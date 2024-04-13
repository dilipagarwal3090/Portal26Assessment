/**
 * --------------------------------------------------------------------------------------------------
 *  This enum is used to represent the dynamodb table names for databases.
 * --------------------------------------------------------------------------------------------------
 */
export enum DynamoDbTableName {
    EventDetails = 'EventDetails',
}

export enum LsiName {
    UserIndex = "UserIndex",
    DomainIndex = "DomainIndex"
}

export enum GsiName {
    EventTimeCategoryIndex = "EventTimeCategoryIndex"
}

export type GsiPerTable = {
    [table in DynamoDbTableName]: Array<GsiName>;
};

export type LsiPerTable = {
    [table in DynamoDbTableName]: Array<LsiName>;
};

export const lsiPerTable: LsiPerTable = {
    EventDetails: [LsiName.UserIndex, LsiName.DomainIndex] as LsiName[],
};

export const gsiPerTable: GsiPerTable = {
    EventDetails: [GsiName.EventTimeCategoryIndex] as GsiName[],
};

/**
 * --------------------------------------------------------------------------------------------------
 *  Scaling type
 * --------------------------------------------------------------------------------------------------
 */
export enum ScalingType {
    READ = 'read',
    WRITE = 'write',
}