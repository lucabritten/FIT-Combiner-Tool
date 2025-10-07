package main.com.fitcombiner.model;

import com.garmin.fit.DateTime;
import com.garmin.fit.File;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FitFile {

    public FitFile(){
        heartRate = new ArrayList<>();
        cadence = new ArrayList<>();
        distance = new ArrayList<>();
        speed = new ArrayList<>();
        latPosition = new ArrayList<>();
        longPosition = new ArrayList<>();
    }

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
    private Float absDistance;
    private Short avgHeartRate;
    private Float avgSpeed; // m/s

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

    @Override
    public String toString() {
        return "FitFile{" +
                "fileType=" + fileType +
                ", manufacturer=" + manufacturer +
                ", product=" + product +
                ", serialNumber=" + serialNumber +
                ", heartRate=" + heartRate +
                ", cadence=" + cadence +
                ", distance=" + distance +
                ", speed=" + speed +
                ", latPosition=" + latPosition +
                ", longPosition=" + longPosition +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", absDistance=" + absDistance +
                ", avgHeartRate=" + avgHeartRate +
                ", avgSpeed=" + avgSpeed +
                '}';
    }
}
