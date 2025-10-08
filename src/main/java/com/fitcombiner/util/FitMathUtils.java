package com.fitcombiner.util;

import com.fitcombiner.model.FitFile;

import java.util.ArrayList;
import java.util.List;

public class FitMathUtils {



    public static Float calcDelta(FitFile firstFile, FitFile secondFile){
        Long start1 = firstFile.getStartTime().getTimestamp();
        Float duration1 = firstFile.getDuration();
        Long start2 = secondFile.getStartTime().getTimestamp();
        return (float) (start2 - start1) - duration1;
    }

    public static Short calcAvgHeartRate(FitFile fitFile){
        List<Short> heartRate = new ArrayList<>(fitFile.getHeartRate());

        return (short) heartRate.stream()
                .mapToInt(Short::intValue)
                .average()
                .orElse(0);
    }

    public static Short calcAvgHeartRate(FitFile firstFile, FitFile secondFile){
        List<Short> heartRate = new ArrayList<>(firstFile.getHeartRate());
        heartRate.addAll(secondFile.getHeartRate());

        return (short) heartRate.stream()
                .mapToInt(Short::intValue)
                .average()
                .orElse(0);
    }

    public static Float calcAvgSpeed(FitFile firstFile, FitFile secondFile){
        List<Float> speed = new ArrayList<>(firstFile.getSpeed());
        speed.addAll(secondFile.getSpeed());

        return (float) speed.stream()
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static Float calcAvgSpeed(FitFile fitFile){
        List<Float> speed = new ArrayList<>(fitFile.getSpeed());

        return (float) speed.stream()
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);
    }
}
