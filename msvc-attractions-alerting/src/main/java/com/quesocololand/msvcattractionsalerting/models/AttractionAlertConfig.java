package com.quesocololand.msvcattractionsalerting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "attraction_alert_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttractionAlertConfig {
    //Fields of AttractionAlertConfig
    @Id
    @Field("_id")
    private String id;

    @Field("attraction_id")
    private String attractionId;

    @Field("congestion_threshold_N")
    private int congestionThresholdN;

    @Field("congestion_period_mins")
    private int congestionPeriodMins;

    @Field("low_occupancy_threshold_M")
    private int lowOccupancyThresholdM;

    @Field("low_occupancy_period_mins")
    private int lowOccupancyPeriodMins;

    // Optional field to determine whether this configuration is the default
    // In case there’s no specific configuration for a attractionId, we could use this.
    @Field("is_global")
    private boolean isGlobal;
}