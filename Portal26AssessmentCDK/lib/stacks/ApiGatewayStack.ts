// ApiGatewayStack.ts

import {CfnOutput, Stack, StackProps} from 'aws-cdk-lib';
import {LambdaIntegration, RestApi} from 'aws-cdk-lib/aws-apigateway';
import {Construct} from "constructs";
import {Function} from 'aws-cdk-lib/aws-lambda';
import {HttpMethod} from "../enums/httpMethod";

interface ApiGatewayStackProps extends StackProps {
    createEventLambda: Function;
    queryDataLambda: Function
}

/**
 * ApiGateway Stack to create ApiGateway related resources.
 */
export class ApiGatewayStack extends Stack {
    constructor(scope: Construct, id: string, props: ApiGatewayStackProps) {
        super(scope, id, props);

        // Define the API Gateway
        const api = new RestApi(this, 'WebhookApi', {
            restApiName: 'Webhook API Gateway',
        });

        const v1Resource = api.root.addResource('v1');

        // Create event resource lambda
        const createEventLambdaIntegration = new LambdaIntegration(props.createEventLambda);

        // Create event resource lambda
        const webhooksResource = v1Resource.addResource('webhooks');
        const tenantResource = webhooksResource.addResource('{tenant_name}');
        const eventsResource = tenantResource.addResource('events');
        eventsResource.addMethod(HttpMethod.POST, createEventLambdaIntegration);


        // Add a resource and method for the query endpoint
        const queryDataLambdaIntegration = new LambdaIntegration(props.queryDataLambda);
        const queryResource = v1Resource.addResource('{tenant_name}').addResource('query');
        queryResource.addMethod(HttpMethod.POST, queryDataLambdaIntegration);

        // Output the API Gateway URL
        new CfnOutput(this, 'ApiGatewayUrl', {
            value: api.url,
        });
    }
}
