package com.britten.fittools.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileData {

    private List<File> files;

    public FileData(){
        files = new ArrayList<>();
    }

    public void addFile(File file){
        files.add(file);
    }

    public List<File> getFiles(){
        return files;
    }

    public void printFiles(){
        files.forEach(System.out::println);
    }
}
