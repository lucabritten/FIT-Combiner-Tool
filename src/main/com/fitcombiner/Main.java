package main.com.fitcombiner;

import main.com.fitcombiner.io.FitDecoder;
import main.com.fitcombiner.io.FitEncoder;
import main.com.fitcombiner.model.FitFile;
import main.com.fitcombiner.service.FitCombinerService;

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
            System.out.println("Gefundene Dateien:");
            System.out.println(" - " + firstFile);
            System.out.println(" - " + secondFile);
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

        String outputPath = new File(firstFile.getParent(), "mergedActivity.fit").getAbsolutePath();

        try{
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
