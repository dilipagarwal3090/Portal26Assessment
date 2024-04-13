import {Duration} from "aws-cdk-lib";
import {Construct} from 'constructs';
import {Code, Function, ILayerVersion, LayerVersion, Runtime} from "aws-cdk-lib/aws-lambda";
import {ManagedPolicy, Role, ServicePrincipal} from "aws-cdk-lib/aws-iam";
import {ComputePlatform, ProfilingGroup} from "aws-cdk-lib/aws-codeguruprofiler";
import {RetentionDays} from "aws-cdk-lib/aws-logs";
import * as path from 'path';

export interface LambdaConstructProps {
    readonly region: string;
    readonly handlerClass: string;
    readonly memorySize: number;
    readonly timeout: Duration;
    readonly functionName: string;
    readonly componentName: string;
    readonly managedPolicies: string[];
}

/**
 * Construct for a Lambda function
 */
export class LambdaConstruct extends Construct {
    readonly lambdaFunction: Function;

    constructor(scope: Construct, name: string, props: LambdaConstructProps) {
        super(scope, name);

        const profilingGroup = createProfilingGroup(this, props.functionName);
        this.lambdaFunction = new Function(this, props.functionName, {
            functionName: props.functionName,
            code: Code.fromAsset(path.resolve(__dirname, '../../../Portal26AssessmentService/target/portal26AssessmentService-1.0.jar')),
            handler: `com.portal26.handler.${props.handlerClass}::handleRequest`,
            layers: [createCodeGuruProfilingLayer(this, props.functionName, props.region)],
            memorySize: props.memorySize,
            logRetention: RetentionDays.ONE_YEAR,
            role: createLambdaFunctionRole(this, `${props.functionName}Role`, props.managedPolicies),
            timeout: props.timeout,
            runtime: Runtime.JAVA_11,
            environment: {
                ...getProfilerEnvVariables(profilingGroup, props.region),
                "Realm": `${props.region}`,
                "FunctionName": name
            }
        });
        profilingGroup.grantPublish(this.lambdaFunction);
    }
}

function createProfilingGroup(construct: Construct, functionName: string): ProfilingGroup {
    const groupName = functionName.concat('ProfilingGroup');
    return new ProfilingGroup(construct, groupName, {
        profilingGroupName: groupName,
        computePlatform: ComputePlatform.AWS_LAMBDA,
    });
}

/**
 * CodeGuru Profiler setup
 * https://docs.aws.amazon.com/codeguru/latest/profiler-ug/lambda-simple.html
 */
function createCodeGuruProfilingLayer(construct: Construct, name: string, region: string): ILayerVersion {
    return LayerVersion.fromLayerVersionArn(
        construct, `${name}ProfilingLayer`,
        `arn:aws:lambda:${region}:157417159150:layer:AWSCodeGuruProfilerJavaAgentLayer:5`,
    );
}

function createLambdaFunctionRole(construct: Construct, rolename: string, managedPolicies: string[]) {
    const role = new Role(construct, rolename, {
        assumedBy: new ServicePrincipal("lambda.amazonaws.com"),
        roleName: rolename
    });
    for (var managedPolicyName of managedPolicies) {
        role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName(managedPolicyName));
    }
    return role;
}

function getProfilerEnvVariables(group: ProfilingGroup, region: string) {
    return {
        "AWS_CODEGURU_PROFILER_TARGET_REGION": region,
        "AWS_CODEGURU_PROFILER_HEAP_SUMMARY_ENABLED": "true",
        "AWS_CODEGURU_PROFILER_GROUP_NAME": group.profilingGroupName
    }
}