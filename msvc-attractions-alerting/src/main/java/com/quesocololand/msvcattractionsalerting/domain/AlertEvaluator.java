package com.quesocololand.msvcattractionsalerting.domain;

import com.quesocololand.msvcattractionsalerting.domain.utils.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvaluator {
    //Fields of AlertEvaluator
    private String attractionId;
    private int attendance;
    private int intervalMinutes;
    private LocalDateTime timestamp;

    //Constructors of AlertEvaluator
    //Field setters of AlertEvaluator (setters)
    //Field getters of AlertEvaluator (getters)
    //Methods of AlertEvaluator
    public Optional<AlertType> evaluateAlertOccupancy(AlertThreshold alertThreshold){
        if(this.attractionId.equals(alertThreshold.getAttractionID())){
            int affluence =  (this.attendance + this.intervalMinutes - 1) / this.intervalMinutes;  //So, it always rounds up

            if (affluence <= alertThreshold.getLowOccupancyThreshold()) {
                return Optional.of(AlertType.LOW_OCCUPANCY);
            } else if (affluence >= alertThreshold.getCongestionThreshold()){
                return Optional.of(AlertType.CONGESTION);
            }
        }
        return Optional.empty();
    }
}
