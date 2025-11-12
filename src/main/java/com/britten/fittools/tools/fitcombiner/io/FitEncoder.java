package com.britten.fittools.tools.fitcombiner.io;

import com.garmin.fit.*;
import com.britten.fittools.tools.fitcombiner.model.FitFile;

import java.io.File;

public class FitEncoder {

    public static void encode(FitFile fitFile, String outputPath){
        try {
            FileEncoder encoder = new FileEncoder(
                    new File(outputPath),
                    Fit.ProtocolVersion.V1_0);

            //File-ID Message
            FileIdMesg fileIdMesg = new FileIdMesg();
            fileIdMesg.setType(fitFile.getFileType());
            fileIdMesg.setManufacturer(fitFile.getManufacturer());
            fileIdMesg.setProduct(fitFile.getProduct());
            fileIdMesg.setSerialNumber(fitFile.getSerialNumber());
            encoder.write(fileIdMesg);

            //Record Messages
            final int recordSize = fitFile.getTimeStamps().size();
            for (int i = 0; i < recordSize; i++) {
                RecordMesg record = new RecordMesg();

                if(i < fitFile.getTimeStamps().size())
                    record.setTimestamp(fitFile.getTimeStamps().get(i));

                if (i < fitFile.getHeartRate().size())
                    record.setHeartRate(fitFile.getHeartRate().get(i));

                if (i < fitFile.getCadence().size())
                    record.setCadence(fitFile.getCadence().get(i));

                if (i < fitFile.getDistance().size())
                    record.setDistance(fitFile.getDistance().get(i));

                if (i < fitFile.getSpeed().size())
                    record.setSpeed(fitFile.getSpeed().get(i));

                if (i < fitFile.getLatPosition().size())
                    record.setPositionLat(fitFile.getLatPosition().get(i));

                if (i < fitFile.getLongPosition().size())
                    record.setPositionLong(fitFile.getLongPosition().get(i));

                encoder.write(record);
            }

            //Session Message
            SessionMesg session = new SessionMesg();
            session.setStartTime(fitFile.getStartTime());
            session.setTotalTimerTime(fitFile.getDuration());
            session.setTotalDistance(fitFile.getAbsDistance());
            session.setAvgHeartRate(fitFile.getAvgHeartRate());
            session.setAvgSpeed(fitFile.getAvgSpeed());
            encoder.write(session);

            System.out.println("TTT:" + session.getTotalTimerTime());

            //Activity Message
            ActivityMesg activity = new ActivityMesg();
            activity.setNumSessions(1);
            activity.setTotalTimerTime(fitFile.getDuration());
            encoder.write(activity);

            encoder.close();
            System.out.printf("FIT file successfully written to %s%n", outputPath);
        }
        catch (Exception e){
            System.err.printf("Error encoding FIT file: %s", e.getMessage());
        }
    }
}
