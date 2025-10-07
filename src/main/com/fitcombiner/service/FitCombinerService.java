package main.com.fitcombiner.service;

import main.com.fitcombiner.model.FitFile;
import main.com.fitcombiner.util.FitMathUtils;

public class FitCombinerService {

    public FitFile merge(FitFile firstFile, FitFile secondFile){
        if(firstFile == null || secondFile == null)
            throw new RuntimeException("Cannot merge null FitFile.");
        if(FitMathUtils.calcDelta(firstFile, secondFile) < 0){
            throw new IllegalArgumentException("Both files are overlapping.");
        }

        FitFile combinedFitFile = new FitFile();

        mergeFileIdMesgData(combinedFitFile,firstFile);

        mergeRecordMesgData(combinedFitFile, firstFile, secondFile);

        mergeSessionMesgData(combinedFitFile, firstFile, secondFile);

        return combinedFitFile;
    }

    private void mergeFileIdMesgData(FitFile combinedFitFile, FitFile firstFile){
        combinedFitFile.setFileType(firstFile.getFileType());
        combinedFitFile.setManufacturer(firstFile.getManufacturer());
        combinedFitFile.setProduct(firstFile.getProduct());
        combinedFitFile.setSerialNumber(firstFile.getSerialNumber());
    }

    private void mergeRecordMesgData(FitFile combinedFitFile, FitFile firstFile, FitFile secondFile){
        combinedFitFile.addHeartRateValues(firstFile.getHeartRate());
        combinedFitFile.addHeartRateValues(secondFile.getHeartRate());

        combinedFitFile.addCadenceValues(firstFile.getCadence());
        combinedFitFile.addCadenceValues(secondFile.getCadence());

        combinedFitFile.addDistanceValues(firstFile.getDistance());
        combinedFitFile.addDistanceValues(secondFile.getDistance());

        combinedFitFile.addSpeedValues(firstFile.getSpeed());
        combinedFitFile.addSpeedValues(secondFile.getSpeed());

        combinedFitFile.addLatPositionValues(firstFile.getLatPosition());
        combinedFitFile.addLatPositionValues(secondFile.getLatPosition());

        combinedFitFile.addLongPositionValues(firstFile.getLongPosition());
        combinedFitFile.addLongPositionValues(secondFile.getLongPosition());
    }

    private void mergeSessionMesgData(FitFile combinedFitFile, FitFile firstFile, FitFile secondFile){
        combinedFitFile.setStartTime(firstFile.getStartTime());

        Float duration = firstFile.getDuration() + secondFile.getDuration();
        combinedFitFile.setDuration(duration);

        Float distance = firstFile.getAbsDistance() + secondFile.getAbsDistance();
        combinedFitFile.setAbsDistance(distance);

        Short avgHeartRate = FitMathUtils.calcAvgHeartRate(firstFile, secondFile);
        combinedFitFile.setAvgHeartRate(avgHeartRate);

        Float avgSpeed = FitMathUtils.calcAvgSpeed(firstFile, secondFile);
        combinedFitFile.setAvgSpeed(avgSpeed);
    }
}
