import { AttributeType } from 'aws-cdk-lib/aws-dynamodb';
import { DynamoDbTableName } from '../../enums/dynamoDb';

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb key. Representing keyName and keyType.
 * --------------------------------------------------------------------------------------------------
 */
export interface DdbKey {
    readonly keyName: string;
    readonly keyType: AttributeType;
}

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb table static properties.
 * --------------------------------------------------------------------------------------------------
 */
export interface DdbStaticProperties {
    readonly partitionKey: DdbKey;
    readonly sortKey?: DdbKey;
    readonly timeToLive?: string;
}

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb table static properties table wise
 * --------------------------------------------------------------------------------------------------
 */
export type DdbStaticPropsPerTable = {
    [table in DynamoDbTableName]: DdbStaticProperties;
};