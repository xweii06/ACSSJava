package repositories;

import Staff.SaleManagement.Appointment;
import utils.DataIO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {
    private static final String FILE_PATH = "appointments.txt";

    public List<Appointment> getAllAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        String content = DataIO.readFile(FILE_PATH);

        if (content == null || content.isEmpty()) {
            return appointments;
        }
        
        for (String line : content.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(",", -1); 
            if (parts.length != 7) {
                System.err.println("Skipping malformed appointment record: " + line);
                continue;
            }

            try {
                Appointment appointment = fromLine(line);
                if (appointment != null) { 
                    appointments.add(appointment);
                }
            } catch (Exception ex) {
                System.err.println("Error parsing appointment record: " + line);
                ex.printStackTrace();
            }
        }
        return appointments;
    }
    
    public List<Appointment> getAllPendingAppointments() throws IOException {
        List<Appointment> allAppointments = getAllAppointments();
        List<Appointment> pendingAppointments = new ArrayList<>();

        for (Appointment appt : allAppointments) {
            if (appt != null && "pending".equalsIgnoreCase(appt.getStatus())) {
                pendingAppointments.add(appt);
            }
        }
        return pendingAppointments;
    }
    
    public List<Appointment> getAppointmentsByCustomerID(String customerID) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
        if (data != null && !data.isEmpty()) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7 && parts[1].equals(customerID)) { // parts[1] is customerID
                        Appointment appt = new Appointment(
                            parts[0], // orderID
                            parts[1], // customerID
                            parts[2], // carID
                            parts[3], // model
                            Double.parseDouble(parts[4]), // price
                            LocalDate.parse(parts[5]), // dueDate
                            parts[6]  // status
                        );
                        appointments.add(appt);
                    }
                }
            }
        }
        return appointments;
    }
    
    public Appointment findAppointmentByCarID(String carID) throws IOException {
        List<Appointment> appointments = getAllAppointments();
        for (Appointment appt : appointments) {
            if (appt.getCarID().equalsIgnoreCase(carID) &&
                    "pending".equalsIgnoreCase(appt.getStatus())) {
                return appt;
            }
        }
        return null;
    }

    public void updateAppointment(Appointment updated) throws IOException {
        List<Appointment> appointments = getAllAppointments();
        List<String> updatedLines = new ArrayList<>();
        
        for (Appointment appt : appointments) {
            if (appt.getOrderID().equals(updated.getOrderID())) {
                updatedLines.add(toDataString(updated));
            } else {
                updatedLines.add(toDataString(appt));
            }
        }
        DataIO.writeFile(FILE_PATH, String.join("\n", updatedLines));
    }

    private String toDataString(Appointment appt) {
        return String.join(",",
            appt.getOrderID(),
            appt.getCustomerID(),
            appt.getCarID(),
            appt.getModel(),
            String.valueOf(appt.getPrice()),
            appt.getDueDate().toString(),
            appt.getStatus()
        );
    }

    private Appointment fromLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 7) {
            System.err.println("Skipping malformed appointment record: " + line);
            return null;
        }

        try {
            return new Appointment(
                parts[0].trim(), // orderID
                parts[1].trim(), // customerID  
                parts[2].trim(), // carID
                parts[3].trim(), // model
                Double.parseDouble(parts[4].trim()), // price
                LocalDate.parse(parts[5].trim()), // dueDate
                parts[6].trim()  // status
            );
        } catch (NumberFormatException | java.time.format.DateTimeParseException ex) {
            System.err.println("Error parsing appointment fields: " + line);
            ex.printStackTrace();
            return null;
        }
    }
}