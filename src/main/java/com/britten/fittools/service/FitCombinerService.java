package com.britten.fittools.service;

import com.britten.fittools.model.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FitCombinerService {

    @Autowired
    private final FileData fileData;

    public FitCombinerService() {
        this.fileData = new FileData();
    }

    public void handleFileUpload(MultipartFile multipartFile){
        try{
            File tempFile = File.createTempFile("fit_upload_", ".fit");

            multipartFile.transferTo(tempFile);

            fileData.addFile(tempFile);

            System.out.println("Uploaded and stored: " + tempFile.getAbsolutePath());
        }
        catch (IOException e){
            throw new RuntimeException("Error while saving: " + multipartFile.getOriginalFilename(), e);
        }
    }

}
