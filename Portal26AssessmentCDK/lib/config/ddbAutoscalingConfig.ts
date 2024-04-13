export interface DdbAutoScaleConfig {
    readonly targetUtilization: number;
    readonly minCapacity: number;
    readonly maxCapacity: number;
}