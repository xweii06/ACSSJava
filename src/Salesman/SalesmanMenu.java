package Salesman;

import java.io.*;
import java.util.*;


public class SalesmanMenu {

    static Map<String, String> profile = new HashMap<>();
    static final String FILE_NAME = "salesman.txt";

    public static void main(String[] args) {
        loadProfile();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Salesman Menu ===");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    editProfile(sc);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 3);
        sc.close();
    }

    static void loadProfile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            profile.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2)
                    profile.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("Error loading profile: " + e.getMessage());
        }
    }

    static void viewProfile() {
        System.out.println("\n--- Profile ---");
        for (Map.Entry<String, String> entry : profile.entrySet()) {
            System.out.printf("%s: %s%n", capitalize(entry.getKey()), entry.getValue());
        }
    }

    static void editProfile(Scanner sc) {
        System.out.print("Enter new name: ");
        profile.put("name", sc.nextLine());
        System.out.print("Enter new email: ");
        profile.put("email", sc.nextLine());
        saveProfile();
        System.out.println("Profile updated!");
    }

    static void saveProfile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : profile.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
