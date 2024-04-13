import {BillingMode, StreamViewType} from 'aws-cdk-lib/aws-dynamodb';

import {DynamoDbTableName, LsiName, GsiName, ScalingType, lsiPerTable, gsiPerTable} from "../enums/dynamoDb";
import {DdbAutoScaleConfig} from "./ddbAutoscalingConfig";
import {GsiStaticProperties, gsiStaticProps, LsiStaticProperties, lsiStaticProps} from "./ddbStaticPropertiesDetails";

export type DdbDynamicPropsPerTable = {
    [table in DynamoDbTableName]: DdbDynamicProperties
};

export type DdbAutoScalingType = {
    [scaling in ScalingType]: DdbAutoScaleConfig;
};

export type PropsPerGsi = {
    [gsi in GsiName]: GsiProperties;
};

export interface GsiProperties {
    readonly staticProps: GsiStaticProperties;
    readonly dynamicProps: GsiDynamicProperties;
}

export interface DdbDynamicProperties {
    readonly isPointInTimeRecovery: boolean;
    readonly billingMode: BillingMode;
    readonly ddbAutoScaling?: DdbAutoScalingType;
    readonly gsiArray: Array<GsiProperties>;
    readonly lsiArray: Array<LsiProperties>;
    readonly streamType?: StreamViewType
}

export interface GsiDynamicProperties {
    readonly readCapacity?: number;
    readonly writeCapacity?: number;
}

export type GsiDynamicPropsPerGsi = {
    [gsi in GsiName]: GsiDynamicProperties;
};

export const GsiDynamicPropsPerGsi: GsiDynamicPropsPerGsi = {
    EventTimeCategoryIndex: {},
};

export const propsPerGSI: PropsPerGsi = {
    EventTimeCategoryIndex: {
        staticProps: gsiStaticProps[GsiName.EventTimeCategoryIndex],
        dynamicProps: GsiDynamicPropsPerGsi[GsiName.EventTimeCategoryIndex],
    },
};

export type LsiPropsPerLsi = {
    [lsiName in LsiName]: LsiProperties;
};

export interface LsiProperties {
    readonly staticProps: LsiStaticProperties;
}

export const lsiPropsPerLsi: LsiPropsPerLsi = {
    UserIndex: {
        staticProps: lsiStaticProps[LsiName.UserIndex],
    },
    DomainIndex: {
        staticProps: lsiStaticProps[LsiName.DomainIndex],
    }
};

function getGSIForTable(tableName: DynamoDbTableName): Array<GsiProperties> {
    const gsiPropsArr: GsiProperties[] = [];
    const gsiNameArr: GsiName[] = gsiPerTable[tableName];
    gsiNameArr.forEach((gsi) => gsiPropsArr.push(propsPerGSI[gsi]));
    return gsiPropsArr;
}

function getLSIForTable(tableName: DynamoDbTableName): Array<LsiProperties> {
    const lsiPropsArr: LsiProperties[] = [];
    const lsiNameArr: LsiName[] = lsiPerTable[tableName];
    lsiNameArr.forEach((lsi) => lsiPropsArr.push(lsiPropsPerLsi[lsi]));
    return lsiPropsArr;
}

// Touchpoint for new ddb table
export const devoDdbDynamicPropsPerTable: DdbDynamicPropsPerTable = {
    EventDetails: {
        isPointInTimeRecovery: true,
        billingMode: BillingMode.PAY_PER_REQUEST,
        gsiArray: getGSIForTable(DynamoDbTableName.EventDetails),
        lsiArray: getLSIForTable(DynamoDbTableName.EventDetails),
        streamType: StreamViewType.NEW_AND_OLD_IMAGES
    },
};