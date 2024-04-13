/**
 * --------------------------------------------------------------------------------------------------
 *  This enum represents the pipeline stage/environment.
 * --------------------------------------------------------------------------------------------------
 */
export enum StageName {
    Beta = 'beta',
}

/**
 * --------------------------------------------------------------------------------------------------
 *  This enum represents the AWS region.
 * --------------------------------------------------------------------------------------------------
 */
export enum AwsRegion {
    EU_WEST_1 = 'eu-west-1',
}

/**
 * --------------------------------------------------------------------------------------------------
 *  Interface for deployment group
 * --------------------------------------------------------------------------------------------------
 */
export interface DeploymentGroup {
    readonly accountId: string;
    readonly region: AwsRegion;
    readonly domain: string;
}

/**
 * --------------------------------------------------------------------------------------------------
 *  Interface for Pipeline environment details
 * --------------------------------------------------------------------------------------------------
 */
export interface Stage {
    readonly name: StageName;
    readonly deploymentGroup: DeploymentGroup[];
    readonly isProd: boolean;
    readonly integTestRequired?: boolean;
    readonly loadTestRequired?: boolean;
    readonly mcmApprovalRequired?: boolean;
}