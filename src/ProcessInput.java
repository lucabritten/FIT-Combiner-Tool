import com.garmin.fit.*;

import java.util.*;
import java.io.*;

public class ProcessInput {

    public List<RecordMesg> fileMesg;
    private Decode decode;
    private MesgBroadcaster mesgBroadcaster;
    private Listener listener;


    public ProcessInput(){
        fileMesg = new ArrayList<>();
        decode = new Decode();
        mesgBroadcaster = new MesgBroadcaster(decode);
        listener = new Listener();
    }

    public static void getFileData(String filepath){
        try {
            Decode decode = new Decode();
            //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
            //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)
            MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);
            Listener listener = new Listener();
            FileInputStream in;

            System.out.printf("FIT Decode Example Application - Protocol %d.%d Profile %d.%d %s\n", Fit.PROTOCOL_VERSION_MAJOR, Fit.PROTOCOL_VERSION_MINOR, Fit.PROFILE_VERSION_MAJOR, Fit.PROFILE_VERSION_MINOR, Fit.PROFILE_TYPE);

            if (filepath == null) {
                System.out.println("Usage: java -jar DecodeExample.jar <filename>");
                return;
            }

            try {
                in = new FileInputStream(filepath);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error opening file " + filepath + " [1]");
            }

            try {
                if (!decode.checkFileIntegrity((InputStream)in)) {
                    throw new RuntimeException("FIT file integrity failed.");
                }
            } catch (RuntimeException e) {
                System.err.print("Exception Checking File Integrity: ");
                System.err.println(e.getMessage());
                System.err.println("Trying to continue...");
            } finally {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                in = new FileInputStream(filepath);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error opening file " + filepath + " [2]");
            }


            mesgBroadcaster.addListener(listener);


            try {
                decode.read(in, mesgBroadcaster, mesgBroadcaster);
            } catch (FitRuntimeException e) {
                // If a file with 0 data size in it's header  has been encountered,
                // attempt to keep processing the file
                if (decode.getInvalidFileDataSize()) {
                    decode.nextFile();
                    decode.read(in, mesgBroadcaster, mesgBroadcaster);
                } else {
                    System.err.print("Exception decoding file: ");
                    System.err.println(e.getMessage());

                    try {
                        in.close();
                    } catch (java.io.IOException f) {
                        throw new RuntimeException(f);
                    }

                    return;
                }
            }

            try {
                in.close();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Decoded FIT file " + filepath + ".");

        } catch (Exception e) {
            System.out.println("Exception decoding file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class Listener implements RecordMesgListener {


        @Override
        public void onMesg(RecordMesg mesg) {
            System.out.println("Record:");

            printValues(mesg, RecordMesg.HeartRateFieldNum);
            printValues(mesg, RecordMesg.CadenceFieldNum);
            printValues(mesg, RecordMesg.DistanceFieldNum);
            printValues(mesg, RecordMesg.SpeedFieldNum);

            printDeveloperData(mesg);
        }

        private void printDeveloperData(Mesg mesg) {
            for (DeveloperField field : mesg.getDeveloperFields()) {
                if (field.getNumValues() < 1) {
                    continue;
                }

                if (field.isDefined()) {
                    System.out.print("   " + field.getName());

                    if (field.getUnits() != null) {
                        System.out.print(" [" + field.getUnits() + "]");
                    }

                    System.out.print(": ");
                } else {
                    System.out.print("   Undefined Field: ");
                }

                System.out.print(field.getValue( 0 ));
                for (int i = 1; i < field.getNumValues(); i++) {
                    System.out.print("," + field.getValue(i));
                }

                System.out.println();
            }
        }



        private void printValues(Mesg mesg, int fieldNum) {
            Iterable<FieldBase> fields = mesg.getOverrideField((short) fieldNum);
            Field profileField = Factory.createField(mesg.getNum(), fieldNum);
            boolean namePrinted = false;


            for (FieldBase field : fields) {
                if (!namePrinted) {
                    System.out.println("   " + profileField.getName() + ":");
                    namePrinted = true;
                }

                if (field instanceof Field) {
                    System.out.println("      native: " + field.getValue());
                } else {
                    System.out.println("      override: " + field.getValue());
                }
            }
        }
    }
}

class Main{


    public static void main(String[] args){
        ProcessInput.getFileData("data/row.fit");
    }
}

