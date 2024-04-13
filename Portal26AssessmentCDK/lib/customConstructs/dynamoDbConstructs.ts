import {Construct} from 'constructs';
import {Attribute, BillingMode, StreamViewType, Table, TableEncryption} from 'aws-cdk-lib/aws-dynamodb';
import {AwsRegion} from '../pipelineDefinition';
import {DynamoDbTableName} from '../enums/dynamoDb';
import {Key} from 'aws-cdk-lib/aws-kms';
import {DdbProperties} from '../types/dynamoDbTypes';
import {ddbPropsPerRegion} from '../config/ddbTableDetails';

/**
 * Interface for dynamodb construct properties.
 */
export interface DynamoDbConstructProps {
    readonly region: AwsRegion;
    readonly table: DynamoDbTableName;
}

/**
 * Construct for a dynamodb tables.
 */
export class DynamoDbConstruct extends Construct {
    public readonly ddbTable: Table;

    constructor(scope: Construct, name: string, props: DynamoDbConstructProps) {
        super(scope, name);
        const ddbProperties: DdbProperties = ddbPropsPerRegion[props.region][props.table];
        this.ddbTable = this.createTable(props, ddbProperties);
        this.addAutoScaling(this.ddbTable, ddbProperties);
        this.addLSI(this.ddbTable, ddbProperties);
        this.addGSI(this.ddbTable, ddbProperties);
    }

    private createTable(props: DynamoDbConstructProps, ddbProperties: DdbProperties): Table {
        let sortKey: Attribute | undefined;
        if (ddbProperties.staticProps.sortKey) {
            sortKey = {
                name: ddbProperties.staticProps.sortKey.keyName,
                type: ddbProperties.staticProps.sortKey.keyType,
            };
        }
        const encryptionKey = new Key(this, `${props.table}Key`, {
            alias: `${props.table}Key`,
            enableKeyRotation: true,
        });
        return new Table(this, `Ddb-${props.table}-${props.region}`, {
            tableName: props.table,
            partitionKey: {
                name: ddbProperties.staticProps.partitionKey.keyName,
                type: ddbProperties.staticProps.partitionKey.keyType,
            },
            sortKey,
            pointInTimeRecovery: true,
            billingMode: BillingMode.PAY_PER_REQUEST,
            stream: StreamViewType.NEW_AND_OLD_IMAGES,
            encryption: TableEncryption.CUSTOMER_MANAGED,
            encryptionKey: encryptionKey,
            timeToLiveAttribute: ddbProperties.staticProps.timeToLive,
            deletionProtection: true,
        });
    }

    private addAutoScaling(ddbTable: Table, ddbProperties: DdbProperties) {
        if (ddbProperties.dynamicProps.ddbAutoScaling
            && ddbProperties.dynamicProps.ddbAutoScaling.read
            && ddbProperties.dynamicProps.ddbAutoScaling.write) {
            const readScaling = ddbTable.autoScaleReadCapacity({
                minCapacity: ddbProperties.dynamicProps.ddbAutoScaling.read.minCapacity,
                maxCapacity: ddbProperties.dynamicProps.ddbAutoScaling.read.maxCapacity,
            });
            readScaling.scaleOnUtilization({
                targetUtilizationPercent: ddbProperties.dynamicProps.ddbAutoScaling.read.targetUtilization,
            });
            const writeScaling = ddbTable.autoScaleWriteCapacity({
                minCapacity: ddbProperties.dynamicProps.ddbAutoScaling.write.minCapacity,
                maxCapacity: ddbProperties.dynamicProps.ddbAutoScaling.write.maxCapacity,
            });
            writeScaling.scaleOnUtilization({
                targetUtilizationPercent: ddbProperties.dynamicProps.ddbAutoScaling.write.targetUtilization,
            });
        } else if (ddbProperties.dynamicProps.billingMode === BillingMode.PROVISIONED) {
            throw new Error(`Read/write auto-scaling is not configured for provisioned table: ${ddbTable.tableName}. `
                + 'Failing to configure the same will result in a policy engine risk.');
        }
    }

    private addLSI(ddbTable: Table, ddbProperties: DdbProperties) {
        ddbProperties.dynamicProps.lsiArray.forEach((lsiProps) => {
            if (lsiProps.staticProps.sortKey && lsiProps.staticProps.sortKey.keyName && lsiProps.staticProps.sortKey.keyType) {
                ddbTable.addLocalSecondaryIndex({
                    indexName: lsiProps.staticProps.indexName,
                    sortKey: {
                        name: lsiProps.staticProps.sortKey.keyName,
                        type: lsiProps.staticProps.sortKey.keyType,
                    },
                });
            }
        });
    }

    private addGSI(ddbTable: Table, ddbProperties: DdbProperties) {
        ddbProperties.dynamicProps.gsiArray.forEach((gsiProps) => {
            if (gsiProps.staticProps.sortKey && gsiProps.staticProps.sortKey.keyName && gsiProps.staticProps.sortKey.keyType) {
                ddbTable.addGlobalSecondaryIndex({
                    indexName: gsiProps.staticProps.indexName,
                    partitionKey: {
                        name: gsiProps.staticProps.partitionKey.keyName,
                        type: gsiProps.staticProps.partitionKey.keyType,
                    },
                    sortKey: {
                        name: gsiProps.staticProps.sortKey.keyName,
                        type: gsiProps.staticProps.sortKey.keyType,
                    },
                    readCapacity: gsiProps.dynamicProps.readCapacity,
                    writeCapacity: gsiProps.dynamicProps.writeCapacity,
                    // Projection Type is ALL by default
                });
            } else {
                ddbTable.addGlobalSecondaryIndex({
                    indexName: gsiProps.staticProps.indexName,
                    partitionKey: {
                        name: gsiProps.staticProps.partitionKey.keyName,
                        type: gsiProps.staticProps.partitionKey.keyType,
                    },
                    readCapacity: gsiProps.dynamicProps.readCapacity,
                    writeCapacity: gsiProps.dynamicProps.writeCapacity,
                    // Projection Type is ALL by default
                });
            }
        });
    }
}