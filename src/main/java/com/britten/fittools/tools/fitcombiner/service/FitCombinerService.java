package com.britten.fittools.tools.fitcombiner.service;

import com.britten.fittools.tools.fitcombiner.io.FitDecoder;
import com.britten.fittools.tools.fitcombiner.io.FitEncoder;
import com.britten.fittools.tools.fitcombiner.model.FitFile;
import com.britten.fittools.tools.fitcombiner.util.FitFileWriter;
import com.britten.fittools.tools.fitcombiner.util.FitMathUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class FitCombinerService {

    public static File mergeAll(List<File> files){
        if(files == null || files.size() < 2 || files.get(0) == null)
            throw new IllegalArgumentException("At least two valid files are required!");

        String outputPath = new File(files.get(0).getParent(), "mergedActivity.fit").getAbsolutePath();

        try {
            List<FitFile> decodedFitFiles = decodeAndSort(files);

            FitFile mergedActivity = decodedFitFiles.stream()
                    .reduce(FitCombinerService::merge)
                    .orElseThrow(() -> new RuntimeException("No files to merge"));

            FitEncoder.encode(mergedActivity, outputPath);

            System.out.println("Merged activities successfully!");
            System.out.println("Output path: " + outputPath);

            return FitFileWriter.writeFitFile(mergedActivity,"combined.fit");
        }
        catch (Exception e) {
            System.err.println("Error while merging: " + e.getMessage());
            throw new RuntimeException("Error while merging FIT files: " + e.getMessage());
        }
    }

    private static List<FitFile> decodeAndSort(List<File> files){
        return files.stream()
                .map(file -> FitDecoder.decode(file.getAbsolutePath()))
                .sorted(Comparator.comparing(FitFile::getStartTime))
                .toList();
    }

    private static FitFile merge(FitFile firstFile, FitFile secondFile){
        if(firstFile == null || secondFile == null)
            throw new IllegalArgumentException("Cannot merge null FitFile.");
        if(FitMathUtils.calcDelta(firstFile, secondFile) < 0)
            throw new IllegalArgumentException("Both files are overlapping.");


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
