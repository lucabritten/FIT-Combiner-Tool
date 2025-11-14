package com.britten.fittools.modules.fitcombiner.adapter.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/tools/fit-combiner")
public class CombineController {

    @Autowired
    private FitCombineHandler fitCombineHandler;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile){
        fitCombineHandler.handleFileUpload(multipartFile);
    }

    @PostMapping("/combine")
    public ResponseEntity<File> combineFiles(){
        File combined = fitCombineHandler.combineFiles();
        return ResponseEntity.ok(combined);
    }

    @GetMapping("/combined/download")
    public ResponseEntity<File> downloadFile(){
        return null;
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getStatus(){
        return ResponseEntity.ok(fitCombineHandler.getStatus());
    }
}
