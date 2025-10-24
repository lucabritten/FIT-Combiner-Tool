package com.fitcombiner;

import com.fitcombiner.util.FileCollector;
import com.fitcombiner.service.FitCombinerService;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<File> directoryFiles = FileCollector.collectFiles(args);
        FitCombinerService.mergeAll(directoryFiles);
    }
}
