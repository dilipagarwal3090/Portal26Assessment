import {App, Stack, StackProps} from 'aws-cdk-lib';
import {AwsRegion} from '../pipelineDefinition';
import {DynamoDbTableName} from '../enums/dynamoDb';
import {DynamoDbConstruct} from '../customConstructs/dynamoDbConstructs';

/**
 * Properties for configuring the dynamoDb stack
 */
export interface DynamoDbStackProps extends StackProps {
    readonly region: AwsRegion;
}

/**
 * Stack used to create database tables.
 */
export class DynamoDbStack extends Stack {
    constructor(parent: App, name: string, props: DynamoDbStackProps) {
        super(parent, name, props);

        Object.values(DynamoDbTableName).forEach((table) => {
            new DynamoDbConstruct(this, `DynamoDb-${table}-${props.region}`, {
                region: props.region,
                table,
            });
        });
    }
}