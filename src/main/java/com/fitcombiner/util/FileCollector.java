package com.fitcombiner.util;

import com.fitcombiner.exception.CollectingFilesException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileCollector {

    public static List<File> collectFiles(String[] args) {
        List<File> directoryFiles;
        if (args.length == 0) {
            directoryFiles = collectFilesFromDirectory();
        }
        else {
            directoryFiles = collectFilesFromParamList(args);
        }

        if (directoryFiles.size() < 2) {
            throw new CollectingFilesException("Add at least two .fit files!");
        }

        System.out.println("Found the following files:");
        for(var file : directoryFiles)
            System.out.println("> " + file);

        return directoryFiles;
    }

    private static List<File> collectFilesFromDirectory(){
        File currentDir = new File(System.getProperty("user.dir"));
        return Arrays.stream(Optional.ofNullable(currentDir.listFiles())
                        .orElseThrow(() -> new CollectingFilesException("Directory is not allowed to be empty!")))
                .filter(f -> f.getName().toLowerCase().endsWith(".fit"))
                .toList();
    }

    private static List<File> collectFilesFromParamList(String[] args){
        return Arrays.stream(args)
                .filter(f -> f.endsWith(".fit"))
                .map(File::new)
                .toList();
    }
}
