import {DdbPropsPerRegion, DdbPropsPerTable} from '../types/dynamoDbTypes';
import {AwsRegion} from '../pipelineDefinition';
import {DynamoDbTableName} from '../enums/dynamoDb';
import {ddbTableStaticProps} from './ddbStaticPropertiesDetails';
import {devoDdbDynamicPropsPerTable} from "./ddbDynamicPropertiesDetails";

/**
 * DynamoDb properties per table wise.
 */
export const ddbPropsPerTable: DdbPropsPerTable = {
    EventDetails: {
        staticProps: ddbTableStaticProps[DynamoDbTableName.EventDetails],
        dynamicProps: devoDdbDynamicPropsPerTable[DynamoDbTableName.EventDetails],
    },
};

/**
 * DynamoDb properties region wise.
 */
export const ddbPropsPerRegion: DdbPropsPerRegion = {
    [AwsRegion.EU_WEST_1]: ddbPropsPerTable,
};