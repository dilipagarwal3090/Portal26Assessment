import { DynamoDbTableName } from '../enums/dynamoDb';
import { AwsRegion, StageName } from '../pipelineDefinition';
import { DdbStaticProperties } from './ddb/ddbStaticPropsType';
import {DdbDynamicProperties} from "../config/ddbDynamicPropertiesDetails";

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb properties stage wise.
 * --------------------------------------------------------------------------------------------------
 */
export type DdbPropsPerStage = {
    [stage in StageName]: DdbPropsPerRegion;
};

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb properties region wise.
 * --------------------------------------------------------------------------------------------------
 */
export type DdbPropsPerRegion = {
    [region in AwsRegion]: DdbPropsPerTable;
};

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb properties
 * --------------------------------------------------------------------------------------------------
 */
export interface DdbProperties {
    readonly staticProps: DdbStaticProperties;
    readonly dynamicProps: DdbDynamicProperties;
}

/**
 * --------------------------------------------------------------------------------------------------
 *  Type to represent dynamoDb properties table wise
 * --------------------------------------------------------------------------------------------------
 */
export type DdbPropsPerTable = {
    [table in DynamoDbTableName]: DdbProperties;
};