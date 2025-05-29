package repositories;

import Staff.SaleManagement.Appointment;
import utils.DataIO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {
    private static final String FILE_PATH = "appointments.txt";
    
    public void addAppointment(Appointment appt) throws IOException {
        DataIO.appendToFile(FILE_PATH, toDataString(appt));
    }

    public List<Appointment> getAllAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        String content = DataIO.readFile(FILE_PATH);
        if (content == null || content.isEmpty()) return appointments;
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                appointments.add(fromLine(line));
            }
        }
        return appointments;
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
        String[] parts = line.split(",");
        if (parts.length != 7) return null;
        
        return new Appointment(
            parts[0], // orderID
            parts[1], // customerID
            parts[2], // carID
            parts[3], // model
            Double.parseDouble(parts[4]), // price
            LocalDate.parse(parts[5]), // dueDate
            parts[6]  // status
        );
    }
}