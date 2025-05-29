package utils;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {
    private static final String FILE_PATH = "data/appointments.txt";

    // Save a new appointment to file
    public static void saveAppointment(String customerId, String carId, String model, String year, String price,
                                       String dueDate, String orderId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.join(",", customerId, carId, model, year, price, dueDate, "PENDING", orderId);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read all appointments
    public static List<String[]> readAppointments(String customerId) {
        List<String[]> list = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8 && parts[0].equals(customerId)) {
                        list.add(parts);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    // Update overdue status safely
    public static void updateOverdueStatus() {
        File file = new File(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        LocalDate today = LocalDate.now();

        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    try {
                        LocalDate dueDate = LocalDate.parse(parts[5]); // Safe parse
                        String status = parts[6];

                        if (status.equalsIgnoreCase("PENDING") && today.isAfter(dueDate)) {
                            parts[6] = "OVERDUE";
                        }
                    } catch (DateTimeParseException ex) {
                        System.err.println("Invalid date format in: " + line);
                    }
                    updatedLines.add(String.join(",", parts));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all car IDs that are currently booked (not cancelled or overdue)
    public static List<String> getBookedCarIds() {
        List<String> bookedCarIds = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return bookedCarIds;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && !parts[6].equalsIgnoreCase("CANCELLED") && !parts[6].equalsIgnoreCase("OVERDUE")) {
                    bookedCarIds.add(parts[1]); // carId
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookedCarIds;
    }
}
