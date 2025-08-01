package com.quesocololand.msvcattractionsalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertThreshold {
    //Fields of AlertThreshold
    private String attractionID;
    private int lowOccupancyThreshold; //people/min
    private int congestionThreshold;    //people/min

    //Constructors of AlertThreshold
    //Field setters of AlertThreshold (setters)
    //Field getters of AlertThreshold (getters)
    //Methods of AlertThreshold
}
