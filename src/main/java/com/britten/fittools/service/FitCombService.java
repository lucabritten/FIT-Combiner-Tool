package com.britten.fittools.service;

import com.britten.fittools.model.FileStorage;
import com.britten.fittools.tools.fitcombiner.model.FitFile;
import com.britten.fittools.tools.fitcombiner.service.FitCombinerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Getter
public class FitCombService {

    @Autowired
    private final FileStorage fileData;
    private String absFilePath;

    public FitCombService() {
        this.fileData = new FileStorage();
    }

    public void handleFileUpload(MultipartFile multipartFile){
        try{
            File tempFile = File.createTempFile("fit_upload_", ".fit");

            multipartFile.transferTo(tempFile);

            fileData.addUploadedFile(tempFile);

            absFilePath = tempFile.getAbsolutePath();
            System.out.println("Uploaded and stored: " + tempFile.getAbsolutePath());
        }
        catch (IOException e){
            throw new RuntimeException("Error while saving: " + multipartFile.getOriginalFilename(), e);
        }
    }

    public FitFile combineFiles(){
        return FitCombinerService.mergeAll(fileData.getUploadedFiles());
    }

}
