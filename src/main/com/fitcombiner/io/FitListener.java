package main.com.fitcombiner.io;

import main.com.fitcombiner.model.FitFile;
import com.garmin.fit.*;

import java.util.ArrayList;
import java.util.List;

public class FitListener implements FileIdMesgListener,
        RecordMesgListener,
        SessionMesgListener {

    private final FitFile fitFile;

    public FitListener(FitFile fitFile) {
        this.fitFile = fitFile;
    }

    @Override
    public void onMesg(FileIdMesg msg) {
        System.out.println("File ID:");

        if (msg.getType() != null) {
            fitFile.setFileType(msg.getType());
        }

        if (msg.getManufacturer() != null) {
            fitFile.setManufacturer(msg.getManufacturer());
        }

        if (msg.getProduct() != null) {
            fitFile.setProduct(msg.getProduct());
        }

        if (msg.getSerialNumber() != null) {
            fitFile.setSerialNumber(msg.getSerialNumber());
        }
    }

    @Override
    public void onMesg(RecordMesg msg) {
        fitFile.addHeartRateValues(combineDatafieldToList(msg, RecordMesg.HeartRateFieldNum, Short.class));
        fitFile.addCadenceValues(combineDatafieldToList(msg, RecordMesg.CadenceFieldNum, Short.class));
        fitFile.addDistanceValues(combineDatafieldToList(msg, RecordMesg.DistanceFieldNum, Float.class));
        fitFile.addSpeedValues(combineDatafieldToList(msg, RecordMesg.SpeedFieldNum, Float.class));
        fitFile.addLatPositionValues(combineDatafieldToList(msg, RecordMesg.PositionLatFieldNum, Integer.class));
        fitFile.addLongPositionValues(combineDatafieldToList(msg, RecordMesg.PositionLongFieldNum, Integer.class));
    }


    @Override
    public void onMesg(SessionMesg msg) {
        System.out.println("Session:");

        if (msg.getStartTime() != null) {
            fitFile.setStartTime(msg.getStartTime());
        }
        if (msg.getTotalElapsedTime() != null) {
            fitFile.setDuration(msg.getTotalElapsedTime());
        }

        if (msg.getTotalDistance() != null) {
            fitFile.setAbsDistance(msg.getTotalDistance());
        }

        if (msg.getAvgSpeed() != null) {
            fitFile.setAvgSpeed(msg.getAvgSpeed());
        }

        if (msg.getAvgHeartRate() != null) {
            fitFile.setAvgHeartRate(msg.getAvgHeartRate());
        }
    }


    @SuppressWarnings("unchecked")
    private <T extends Number> List<T> combineDatafieldToList(Mesg mesg, int fieldNum, Class<T> type) {
        List<T> values = new ArrayList<>();
        FieldBase field = mesg.getField((short) fieldNum);

        for (int i=0; i < field.getNumValues(); i++) {
            Object value = field;
            if(type.isInstance(value))
                values.add((T) value);
        }
        return values;
    }
}