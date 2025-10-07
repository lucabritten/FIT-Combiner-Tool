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

        if (msg.getTotalDistance() != null && msg.getTotalDistance() != 0)
            fitFile.setAbsDistance(msg.getTotalDistance());
        else {
            Float absDistance = fitFile.getDistance().stream()
                    .reduce((float) 0.0, Float::sum);
            fitFile.setAbsDistance(absDistance);
        }

        if (msg.getAvgSpeed() != null && msg.getAvgSpeed() != 0)
            fitFile.setAvgSpeed(msg.getAvgSpeed());

        else if(fitFile.getDuration() != null && fitFile.getAbsDistance() != null){
            float avgSpeed =  fitFile.getAbsDistance() / fitFile.getDuration();
            fitFile.setAvgSpeed(avgSpeed);
        }
        else{
            double avgSpeed = fitFile.getSpeed().stream()
                    .mapToDouble(Float::doubleValue)
                    .average()
                    .orElse(0.0);
            fitFile.setAvgSpeed((float) avgSpeed);
        }

        if (msg.getAvgHeartRate() != null && msg.getAvgHeartRate() != 0)
            fitFile.setAvgHeartRate(msg.getAvgHeartRate());
        else{
            double avgHeartRate = fitFile.getHeartRate().stream()
                    .mapToInt(Short::intValue)
                    .average()
                    .orElse(0.0);
            fitFile.setAvgHeartRate((short) Math.round(avgHeartRate));
        }
        System.out.println(fitFile);
    }


    @SuppressWarnings("unchecked")
    private <T extends Number> List<T> combineDatafieldToList(Mesg mesg, int fieldNum, Class<T> type) {
        List<T> values = new ArrayList<>();
        FieldBase field = mesg.getField((short) fieldNum);

        if(field == null)
            return values;

        for (int i=0; i < field.getNumValues(); i++) {
            Object value = field.getValue(i);
            if(type.isInstance(value))
                values.add((T) value);
        }
        return values;
    }
}