package com.britten.fittools.tools.fitcombiner;

import com.britten.fittools.tools.fitcombiner.util.FileCollector;
import com.britten.fittools.tools.fitcombiner.service.FitCombinerService;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<File> directoryFiles = FileCollector.collectFiles(args);
        FitCombinerService.mergeAll(directoryFiles);
    }
}
