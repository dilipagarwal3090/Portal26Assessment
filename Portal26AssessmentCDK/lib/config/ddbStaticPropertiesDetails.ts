import {AttributeType} from 'aws-cdk-lib/aws-dynamodb';
import {DdbStaticPropsPerTable} from '../types/ddb/ddbStaticPropsType';
import {GsiName, LsiName} from "../enums/dynamoDb";

/**
 * Represents dynamodb static properties table wise
 */
export const ddbTableStaticProps: DdbStaticPropsPerTable = {
    EventDetails: {
        partitionKey: {
            keyName: 'eventId',
            keyType: AttributeType.STRING,
        },
        sortKey: {
            keyName: 'eventTimeStamp',
            keyType: AttributeType.NUMBER,
        },
        timeToLive: "ttl"
    },
};

export interface DdbPrimaryKey {
    readonly keyName: string;
    readonly keyType: AttributeType;
}

export interface DdbStaticProperties {
    readonly partitionKey: DdbPrimaryKey;
    readonly sortKey?: DdbPrimaryKey;
    readonly timeToLive?: string;
}

export interface GsiStaticProperties {
    readonly indexName: string;
    readonly partitionKey: DdbPrimaryKey;
    readonly sortKey?: DdbPrimaryKey;
}

export type GsiStaticPropsPerGsi = {
    [gsi in GsiName]: GsiStaticProperties
};


export interface LsiStaticProperties {
    readonly indexName: string;
    readonly sortKey?: DdbPrimaryKey;
}

export type LsiStaticPropsPerLsi = {
    [lsi in LsiName]: LsiStaticProperties
};

export const lsiStaticProps: LsiStaticPropsPerLsi = {
    UserIndex: {
        indexName: LsiName.UserIndex,
        sortKey: {
            keyName: "userId",
            keyType: AttributeType.STRING
        }
    },
    DomainIndex: {
        indexName: LsiName.DomainIndex,
        sortKey: {
            keyName: "domain",
            keyType: AttributeType.STRING
        }
    }
}

export const gsiStaticProps: GsiStaticPropsPerGsi = {
    EventTimeCategoryIndex: {
        indexName: GsiName.EventTimeCategoryIndex,
        partitionKey: {
            keyName: "category",
            keyType: AttributeType.STRING
        },
        sortKey: {
            keyName: "eventTimeStamp",
            keyType: AttributeType.NUMBER
        }
    },
};