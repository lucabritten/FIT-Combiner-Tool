package com.fitcombiner;

import com.fitcombiner.exception.CollectingFilesException;
import com.fitcombiner.service.FitCombinerService;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<File> directoryFiles = collectFiles(args);
        FitCombinerService.mergeAll(directoryFiles);

    }

    private static List<File> collectFiles(String[] args) {
        List<File> directoryFiles;

        if (args.length == 0) {
            directoryFiles = collectFilesFromDirectory();
        }
        else {
            directoryFiles = collectFilesFromParamList(args);
        }

        if (directoryFiles.size() < 2) {
            throw new CollectingFilesException("Add at least two .fit files are required!");
        }

        System.out.println("Found the following files:");
        for(var file : directoryFiles)
            System.out.println("> " + file);

        return directoryFiles;
    }

    private static List<File> collectFilesFromDirectory(){
        File currentDir = new File(System.getProperty("user.dir"));
        return Arrays.stream(currentDir.listFiles())
                .filter(f -> f.getName().toLowerCase().endsWith(".fit"))
                .toList();
    }

    private static List<File> collectFilesFromParamList(String[] args){
        List<File> files = new LinkedList<>();
        for (var arg : args) {
            files.add(new File(arg));
        }
        return files;
    }
}
