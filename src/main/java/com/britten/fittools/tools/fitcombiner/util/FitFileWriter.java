package com.britten.fittools.tools.fitcombiner.util;

import com.britten.fittools.tools.fitcombiner.model.FitFile;
import com.garmin.fit.*;


public class FitFileWriter {

    public static java.io.File writeFitFile(FitFile fitFile, String outputPath){
        java.io.File output = new java.io.File(outputPath);

        FileEncoder encoder;
        try {
            encoder = new FileEncoder(output, Fit.ProtocolVersion.V2_0);

            for (int i = 0; i < fitFile.getTimeStamps().size(); i++) {
                RecordMesg record = new RecordMesg();
                record.setTimestamp(fitFile.getTimeStamps().get(i));
                record.setHeartRate(fitFile.getHeartRate().get(i));
                record.setCadence(fitFile.getCadence().get(i));
                record.setDistance(fitFile.getDistance().get(i));
                record.setSpeed(fitFile.getSpeed().get(i));
                record.setPositionLat(fitFile.getLatPosition().get(i));
                record.setPositionLong(fitFile.getLongPosition().get(i));
                encoder.write(record);
            }

            // Optional: Session Message hinzufügen
            SessionMesg session = new SessionMesg();
            session.setStartTime(fitFile.getStartTime());
            session.setTotalTimerTime(fitFile.getDuration());
            session.setTotalDistance(fitFile.getAbsDistance());
            session.setAvgSpeed(fitFile.getAvgSpeed());
            encoder.write(session);

            encoder.close();

            return output;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}