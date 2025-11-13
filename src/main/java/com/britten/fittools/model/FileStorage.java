package com.britten.fittools.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class FileStorage {

    private final List<File> uploadedFiles;
    private File lastCombinedFile;


    public FileStorage(){
        uploadedFiles = new ArrayList<>();
    }

    public void addUploadedFile(File file){
        uploadedFiles.add(file);
    }

    public void addAllUploadedFiles(List<File> files){
        uploadedFiles.addAll(files);
    }

    public void clearUploads(){
        uploadedFiles.forEach(File::delete);
        uploadedFiles.clear();
    }

    public List<String> getUploadedFileNames(){
        return uploadedFiles.stream()
                .map(File::getName)
                .toList();
    }

}
