package main.com.fitcombiner.model;

import com.garmin.fit.DateTime;
import com.garmin.fit.File;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class FitFile {

    //get Data from FileIdMesgListener
    private File fileType;
    private Integer manufacturer;
    private Integer product;
    private Long serialNumber;

    //get Data from RecordMesgListener
    private List<Short> heartRate;
    private List<Short> cadence;
    private List<Float> distance;
    private List<Float> speed;
    private List<Integer> latPosition;
    private List<Integer> longPosition;

    //get Data from SessionMesgListener
    private DateTime startTime;
    private Float duration;
    private Float absDistance;//m/s
    private Short avgHeartRate;
    private Float avgSpeed;

    public void addHeartRateValues(List<Short> list){
        heartRate.addAll(list);
    }
    public void addCadenceValues(List<Short> list){
        cadence.addAll(list);
    }
    public void addDistanceValues(List<Float> list){
        distance.addAll(list);
    }
    public void addSpeedValues(List<Float> list){
        speed.addAll(list);
    }

    public void addLatPositionValues(List<Integer> list){
        latPosition.addAll(list);
    }

    public void addLongPositionValues(List<Integer> list){
        longPosition.addAll(list);
    }
}
