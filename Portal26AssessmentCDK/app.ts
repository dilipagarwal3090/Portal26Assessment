#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import {Portal26AssessmentLambdaStack} from './lib/stacks/Portal26AssessmentLambdaStack';
import {ApiGatewayStack} from "./lib/stacks/ApiGatewayStack";
import {DynamoDbStack} from "./lib/stacks/dynamoDbStack";
import {AwsRegion} from "./lib/pipelineDefinition";

const app = new cdk.App();

const lambdaStack =
    new Portal26AssessmentLambdaStack(app, 'Portal26AssessmentLambdaStack', {
        region: AwsRegion.EU_WEST_1
    });

new ApiGatewayStack(app, 'ApiGatewayStack', {
    createEventLambda: lambdaStack.createEventLambda,
    queryDataLambda: lambdaStack.queryEventLambda,
});

const dynamoDbStack = new DynamoDbStack(app, `DynamoDbStack`, {
    region: AwsRegion.EU_WEST_1,
});

