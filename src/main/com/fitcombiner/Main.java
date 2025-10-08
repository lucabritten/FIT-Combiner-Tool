package main.com.fitcombiner;

import main.com.fitcombiner.io.FitDecoder;
import main.com.fitcombiner.model.FitFile;

public class Main {


    public static void main(String[] args){
        FitFile decodedFile = FitDecoder.decode("src/main/ressource/row.fit");
        //TODO: Fit files need to be auto-sorted (firstFile/secondFile) --> start Timestamp
    }
}
