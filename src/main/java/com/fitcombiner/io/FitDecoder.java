package com.fitcombiner.io;

import com.garmin.fit.*;
import com.fitcombiner.model.FitFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



public class FitDecoder {

    public static FitFile decode(String filepath){
        FitFile fitFile = new FitFile();
        try {
            Decode decode = new Decode();
            MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);
            FitListener listener = new FitListener(fitFile);
            FileInputStream in;

            if (filepath == null) {
                throw new IllegalArgumentException("Please enter a valid filepath");
            }

            try {
                in = new FileInputStream(filepath);
            } catch (IOException e) {
                throw new IOException("Error opening file: " + filepath);
            }

            try {
                if (!decode.checkFileIntegrity((InputStream) in))
                    throw new RuntimeException("FIT file integrity failed");
            } catch (RuntimeException e) {
                System.err.print("Exception Checking File Integrity: ");
                System.err.println(e.getMessage());
                System.err.println("Trying to continue...");
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                in = new FileInputStream(filepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mesgBroadcaster.addListener((FileIdMesgListener) listener);
            mesgBroadcaster.addListener((RecordMesgListener) listener);
            mesgBroadcaster.addListener((SessionMesgListener) listener);


            try {
                decode.read(in, mesgBroadcaster, mesgBroadcaster);
            } catch (FitRuntimeException e) {
                if (decode.getInvalidFileDataSize()) {
                    decode.nextFile();
                    decode.read(in, mesgBroadcaster, mesgBroadcaster);
                } else {
                    System.err.print("Exception decoding file: ");
                    System.err.println(e.getMessage());

                    try {
                        in.close();
                    } catch (IOException f) {
                        throw new RuntimeException(f);
                    }
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Decoded FIT file " + filepath + ".");
            System.out.println(fitFile.getDuration());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return fitFile;
    }
}