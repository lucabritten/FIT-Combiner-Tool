package main.com.fitcombiner;

import main.com.fitcombiner.io.FitDecoder;
import main.com.fitcombiner.io.FitEncoder;
import main.com.fitcombiner.model.FitFile;
import main.com.fitcombiner.service.FitCombinerService;

public class Main {


    public static void main(String[] args){
        FitFile decodedFile1 = FitDecoder.decode("src/main/ressource/row1.fit");
        FitFile decodedFile2 = FitDecoder.decode("src/main/ressource/row2.fit");

        FitFile file = FitCombinerService.merge(decodedFile1,decodedFile2);

        FitEncoder.encode(file, "src/main/ressource/comb.fit");
        //TODO: Fit files need to be auto-sorted (firstFile/secondFile) --> start Timestamp
    }
}
