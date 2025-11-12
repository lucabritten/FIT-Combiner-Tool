package com.britten.fittools.controller;

import com.britten.fittools.service.FitCombService;
import com.britten.fittools.tools.fitcombiner.model.FitFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/tools/fit-combiner")
public class FitCombinerController {

    @Autowired
    private FitCombService fitCombinerService;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile){
        fitCombinerService.handleFileUpload(multipartFile);
    }

    @PostMapping("/combine")
    public ResponseEntity<FitFile> combineFiles(){
        FitFile combined = fitCombinerService.combineFiles();
        return ResponseEntity.ok(combined);
    }
}
