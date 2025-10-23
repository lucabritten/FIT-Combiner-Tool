package com.fitcombiner.service;

import com.fitcombiner.io.FitDecoder;
import com.fitcombiner.io.FitEncoder;
import com.fitcombiner.model.FitFile;
import com.fitcombiner.util.FitMathUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class FitCombinerService {

    public static void mergeAll(List<File> files){

        try {
            String outputPath = new File(files.get(0).getParent(), "mergedActivity.fit").getAbsolutePath();

            List<FitFile> decodedFitFiles = files.stream()
                    .map(file -> FitDecoder.decode(file.getAbsolutePath()))
                    .sorted(Comparator.comparing(FitFile::getStartTime))
                    .toList();

            FitFile mergedActivity = decodedFitFiles.stream()
                    .reduce(FitCombinerService::merge)
                    .orElseThrow(() -> new RuntimeException("No files to merge"));

            FitEncoder.encode(mergedActivity, outputPath);

            System.out.println("Merged activities successfully!");
            System.out.println("Output path: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error while merging: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static FitFile merge(FitFile firstFile, FitFile secondFile){
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

    private static void mergeFileIdMesgData(FitFile combinedFitFile, FitFile firstFile){
        combinedFitFile.setFileType(firstFile.getFileType());
        combinedFitFile.setManufacturer(firstFile.getManufacturer());
        combinedFitFile.setProduct(firstFile.getProduct());
        combinedFitFile.setSerialNumber(firstFile.getSerialNumber());
    }

    private static void mergeRecordMesgData(FitFile combinedFitFile, FitFile firstFile, FitFile secondFile){
        combinedFitFile.addTimeStampValues(firstFile.getTimeStamps());
        combinedFitFile.addTimeStampValues(secondFile.getTimeStamps());

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

    private static void mergeSessionMesgData(FitFile combinedFitFile, FitFile firstFile, FitFile secondFile){
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
