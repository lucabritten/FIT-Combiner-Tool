package com.fitcombiner.model;

import com.garmin.fit.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FitFile {

    //get Data from FileIdMesgListener
    private Short fileType;
    private Short manufacturer;
    private Short product;
    private Long serialNumber;

    //get Data from DeviceInfoMesgListener
    private DateTime timeStamp;

    //get Data from RecordMesgListener
    private List<Short> heartRate;
    private List<Float> cadence;
    private List<Float> segmentDistance;
    private List<Float> speed;

    //get Data from SessionMesgListener
    private DateTime startTime;
    private Float duration;
    private Float absDistance; //m/s
}
