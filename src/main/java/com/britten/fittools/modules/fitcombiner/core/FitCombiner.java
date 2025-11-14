package com.britten.fittools.modules.fitcombiner.core;

import com.britten.fittools.modules.FitTool;

import java.io.File;
import java.util.List;

public class FitCombiner implements FitTool {

    @Override
    public File execute(List<File> files){
        return FitCombinerService.mergeAll(files);
    }
}
