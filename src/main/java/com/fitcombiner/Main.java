package com.fitcombiner;

import com.fitcombiner.io.FitDecoder;
import com.fitcombiner.io.FitEncoder;
import com.fitcombiner.model.FitFile;
import com.fitcombiner.service.FitCombinerService;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args){

        File firstFile = null;
        File secondFile = null;

        if(args.length == 2){
            firstFile = new File(args[0]);
            secondFile = new File(args[1]);
            System.out.println("Attached files:");
            System.out.println(" - " + firstFile);
            System.out.println(" - " + secondFile);
        }
        else if (args.length > 2) {
            System.out.println("Attached files:");
            List<File> files = Arrays.stream(args)
                    .map(File::new)
                    .toList();

            files.forEach(file -> System.out.println(" - " + file));

            try {
                FitFile mergedActivity = FitDecoder.decode(files.get(0).getAbsolutePath());

                for (int i = 1; i < files.size(); i++) {
                    FitFile nextFile = FitDecoder.decode(files.get(i).getAbsolutePath());
                    mergedActivity = FitCombinerService.merge(mergedActivity, nextFile);
                }

                String outputPath = new File(files.get(0).getParent(), "mergedActivity.fit").getAbsolutePath();
                FitEncoder.encode(mergedActivity, outputPath);

                System.out.println("Merged " + files.size() + " activities successfully!");
                System.out.println("Output path: " + outputPath);

            } catch (Exception e) {
                System.err.println("Error while merging multiple files: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            File currentDir = new File(System.getProperty("user.dir"));
            List<File> fitFiles = Arrays.stream(currentDir.listFiles())
                    .filter(f -> f.getName().toLowerCase().endsWith(".fit"))
                    .sorted(Comparator.comparing(File::getName))
                    .toList();

            if (fitFiles.size() < 2) {
                System.err.println("Bitte lege zwei FIT Dateien in den Ordner!");
                return;
            }

            firstFile = fitFiles.get(0);
            secondFile = fitFiles.get(1);
            System.out.println("Gefundene Dateien:");
            System.out.println(" - " + firstFile);
            System.out.println(" - " + secondFile);
        }

        try{
            String outputPath = new File(firstFile.getParent(), "mergedActivity.fit").getAbsolutePath();

            FitFile decodedFile1 = FitDecoder.decode(firstFile.getAbsolutePath());
            FitFile decodedFile2 = FitDecoder.decode(secondFile.getAbsolutePath());
            FitFile mergedActivity = FitCombinerService.merge(decodedFile1, decodedFile2);
            FitEncoder.encode(mergedActivity, outputPath);

            System.out.println("Merged activities successfully!");
            System.out.println("Output path: " + outputPath);
        }
        catch (Exception e){
            System.err.println("Error while merging: " + e.getMessage());
            e.printStackTrace();
        }

        //TODO: Fit files need to be auto-sorted (firstFile/secondFile) --> start Timestamp
    }
}
