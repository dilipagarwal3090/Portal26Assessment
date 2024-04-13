import {Duration, Stack, StackProps} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {
    Function, Code, Runtime
} from 'aws-cdk-lib/aws-lambda';
import * as path from 'path';
import {LambdaConstruct} from "../customConstructs/lambdaConstruct";

interface Portal26AssessmentLambdaStackProps extends StackProps {
    region: string
}

/**
 * Portal26AssessmentLambdaStack to create lambda requires by application
 */
export class Portal26AssessmentLambdaStack extends Stack {

    readonly createEventLambda: Function;
    readonly queryEventLambda: Function;

    constructor(scope: Construct, id: string, props: Portal26AssessmentLambdaStackProps) {
        super(scope, id, props);

        //******************************************************************************
        // CreateEvent Lambda Handler
        //******************************************************************************
        const createEventLambdaHandlerLambdaConstruct = new LambdaConstruct(this, "CreateEventLambdaHandler", {
            region: props.region,
            handlerClass: 'CreateEventHandler',
            memorySize: 512,
            timeout: Duration.seconds(50),
            functionName: "CreateEventHandler",
            componentName: "CreateEventHandler",
            managedPolicies: [
                "service-role/AWSLambdaBasicExecutionRole",
                "AmazonDynamoDBFullAccess",
            ]
        });
        this.createEventLambda = createEventLambdaHandlerLambdaConstruct.lambdaFunction;


        //******************************************************************************
        // QueryEvent Lambda Handler
        //******************************************************************************
        const queryEventLambdaHandlerLambdaConstruct = new LambdaConstruct(this, "QueryEventLambdaHandler", {
            region: props.region,
            handlerClass: 'QueryEventHandler',
            memorySize: 512,
            timeout: Duration.seconds(50),
            functionName: "QueryEventHandler",
            componentName: "QueryEventHandler",
            managedPolicies: [
                "service-role/AWSLambdaBasicExecutionRole",
                "AmazonDynamoDBFullAccess",
            ]
        });
        this.queryEventLambda = queryEventLambdaHandlerLambdaConstruct.lambdaFunction;
    }
}

