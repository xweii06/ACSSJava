package utils;

public class InputValidator {
    public static void validateName(String name) throws InvalidInputException {
        if (name.length() < 2 || name.length() > 20) {
            throw new InvalidInputException("Name must be between 2 and 20 characters");
        }
        if (!name.matches("^[A-Za-z ]+$")) {
            throw new InvalidInputException("Name can only contain letters and spaces");
        }
    }
    
    public static void validatePW(String password) throws InvalidInputException {
        if (password.length() < 4 || password.length() > 12) {
            throw new InvalidInputException("Password must be between 4 and 12 characters");
        }
    }
    
    public static String parsePhoneFormat(String phone) {
        // remove spaces and dashes
        phone = phone.replaceAll("[\\s\\-]", "");
        
        // Convert local format to international (+60)
        if (phone.matches("^0[1-9][0-9]{7,9}$")) {
            phone = "+60" + phone.substring(1); 
        } else if (phone.matches("^60[1-9][0-9]{7,9}$")) {
            phone = "+" + phone;  
        } 
        return phone;
    }
    
    public static void validatePhone(String phone) throws InvalidInputException {
        String newPhone = parsePhoneFormat(phone);
        if (!newPhone.matches("^\\+60(1[0-9]{8,9}|[3-9][0-9]{7,8})$")) {
            throw new InvalidInputException(
                    "Please enter a valid Malaysian phone number.");
        }
    }

    public static void validateEmail(String email) throws InvalidInputException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Invalid email format");
        }
    }
}
