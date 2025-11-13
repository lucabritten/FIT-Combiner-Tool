package com.britten.fittools.controller;

import com.britten.fittools.service.FitCombService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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
    public ResponseEntity<File> combineFiles(){
        File combined = fitCombinerService.combineFiles();
        return ResponseEntity.ok(combined);
    }

    @GetMapping("/combined/download")
    public ResponseEntity<File> downloadFile(){
        return null;
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getStatus(){
        return ResponseEntity.ok(fitCombinerService.getStatus());
    }
}
