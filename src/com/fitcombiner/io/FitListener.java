package com.fitcombiner.io;

import com.fitcombiner.model.FitFile;
import com.garmin.fit.*;

public class Listener implements FileIdMesgListener,
        DeviceInfoMesgListener,
        RecordMesgListener,
        SessionMesgListener {

    private FitFile fitFile;

    public Listener(FitFile fitFile) {
        this.fitFile = fitFile;
    }

    @Override
    public void onMesg(FileIdMesg mesg) {
        System.out.println("File ID:");

        if (mesg.getType() != null) {

        }

        if (mesg.getManufacturer() != null) {
            System.out.print("   Manufacturer: ");
            System.out.println(mesg.getManufacturer());
        }

        if (mesg.getProduct() != null) {
            System.out.print("   Product: ");
            System.out.println(mesg.getProduct());
        }

        if (mesg.getSerialNumber() != null) {
            System.out.print("   Serial Number: ");
            System.out.println(mesg.getSerialNumber());
        }

        if (mesg.getNumber() != null) {
            System.out.print("   Number: ");
            System.out.println(mesg.getNumber());
        }
    }

    @Override
    public void onMesg(UserProfileMesg mesg) {
        System.out.println("User profile:");

        if (mesg.getFriendlyName() != null) {
            System.out.print("   Friendly Name: ");
            System.out.println(mesg.getFriendlyName());
        }

        if (mesg.getGender() != null) {
            if (mesg.getGender() == Gender.MALE) {
                System.out.println("   Gender: Male");
            } else if (mesg.getGender() == Gender.FEMALE) {
                System.out.println("   Gender: Female");
            }
        }

        if (mesg.getAge() != null) {
            System.out.print("   Age [years]: ");
            System.out.println(mesg.getAge());
        }

        if (mesg.getWeight() != null) {
            System.out.print("   Weight [kg]: ");
            System.out.println(mesg.getWeight());
        }
    }

    @Override
    public void onMesg(DeviceInfoMesg mesg) {
        System.out.println("Device info:");

        if (mesg.getTimestamp() != null) {
            System.out.print("   Timestamp: ");
            System.out.println(mesg.getTimestamp());
        }

        if (mesg.getBatteryStatus() != null) {
            System.out.print("   Battery status: ");

            switch (mesg.getBatteryStatus()) {
                case BatteryStatus.CRITICAL:
                    System.out.println("Critical");
                    break;
                case BatteryStatus.GOOD:
                    System.out.println("Good");
                    break;
                case BatteryStatus.LOW:
                    System.out.println("Low");
                    break;
                case BatteryStatus.NEW:
                    System.out.println("New");
                    break;
                case BatteryStatus.OK:
                    System.out.println("OK");
                    break;
                default:
                    System.out.println("Invalid");
                    break;
            }
        }
    }


    @Override
    public void onMesg(RecordMesg mesg) {
        System.out.println("Record:");

        printValues(mesg, RecordMesg.HeartRateFieldNum);
        printValues(mesg, RecordMesg.CadenceFieldNum);
        printValues(mesg, RecordMesg.DistanceFieldNum);
        printValues(mesg, RecordMesg.SpeedFieldNum);

        printDeveloperData(mesg);
    }

    @Override
    public void onMesg(SessionMesg mesg) {
        System.out.println("Session:");

        // Start time
        if (mesg.getStartTime() != null) {
            System.out.println("   Startzeit: " + mesg.getStartTime());
        }
        // Dauer (TotalElapsedTime)
        if (mesg.getTotalElapsedTime() != null) {
            double seconds = mesg.getTotalElapsedTime();
            System.out.printf("   Dauer: %.1f Sekunden\n", seconds);
        }
        // Distanz (TotalDistance)
        if (mesg.getTotalDistance() != null) {
            double meters = mesg.getTotalDistance();
            System.out.printf("   Distanz: %.2f Meter\n", meters);
        }
        // Durchschnittsgeschwindigkeit (AvgSpeed)
        if (mesg.getAvgSpeed() != null) {
            double speed = mesg.getAvgSpeed(); // m/s
            System.out.printf("   Durchschnittsgeschwindigkeit: %.2f m/s\n", speed);
        }
        // Durchschnittspuls (AvgHeartRate)
        if (mesg.getAvgHeartRate() != null) {
            System.out.println("   Durchschnittspuls: " + mesg.getAvgHeartRate() + " bpm");
        }
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

            System.out.print(field.getValue(0));
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

        if (profileField == null) {
            return;
        }

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