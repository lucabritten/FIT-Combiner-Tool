package com.britten.fittools.service;

import com.britten.fittools.model.FileStorage;
import com.britten.fittools.tools.fitcombiner.service.FitCombinerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

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

    public File combineFiles(){
        fileData.setLastCombinedFile(FitCombinerService.mergeAll(fileData.getUploadedFiles()));
        fileData.clearUploads();
        return null;
    }

    public File getCombinedFile(){
        if(fileData.getLastCombinedFile() != null)
            return fileData.getLastCombinedFile();

        throw new NoSuchElementException("No combined file exists.");
    }

    public List<String> getStatus(){
        return fileData.getUploadedFileNames();
    }

}
