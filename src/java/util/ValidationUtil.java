package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    // Email validation
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    
    // Phone validation (Vietnamese phone numbers)
    private static final String PHONE_PATTERN = "^(0|\\+84)[3-9]\\d{8}$";
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    
    // Check if string is null or empty
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    // Check if string is not null and not empty
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    // Validate email format
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return emailPattern.matcher(email).matches();
    }
    
    // Validate phone number format
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return phonePattern.matcher(phone).matches();
    }
    
    // Validate date format (dd/MM/yyyy)
    public static boolean isValidDate(String dateStr) {
        if (isEmpty(dateStr)) return false;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Validate date range
    public static boolean isDateInRange(Date date, Date minDate, Date maxDate) {
        if (date == null) return false;
        
        if (minDate != null && date.before(minDate)) return false;
        if (maxDate != null && date.after(maxDate)) return false;
        
        return true;
    }
    
    // Check if date is in the future
    public static boolean isFutureDate(Date date) {
        if (date == null) return false;
        return date.after(new Date());
    }
    
    // Check if date is in the past
    public static boolean isPastDate(Date date) {
        if (date == null) return false;
        return date.before(new Date());
    }
    
    // Validate numeric string
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) return false;
        
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validate integer string
    public static boolean isInteger(String str) {
        if (isEmpty(str)) return false;
        
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validate positive number
    public static boolean isPositiveNumber(String str) {
        if (!isNumeric(str)) return false;
        
        try {
            double num = Double.parseDouble(str);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validate string length
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) return false;
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
    
    // Validate password strength
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password)) return false;
        
        // At least 8 characters
        if (password.length() < 8) return false;
        
        // Contains at least one digit
        if (!password.matches(".*\\d.*")) return false;
        
        // Contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) return false;
        
        // Contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) return false;
        
        return true;
    }
    
    // Validate username format
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) return false;
        
        // 3-20 characters, alphanumeric and underscore only
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    // Validate salary range
    public static boolean isValidSalary(String salaryStr) {
        if (!isNumeric(salaryStr)) return false;
        
        try {
            double salary = Double.parseDouble(salaryStr);
            // Minimum wage in Vietnam is around 4.68 million VND
            return salary >= 1000000 && salary <= 100000000; // 1M to 100M VND
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validate age range
    public static boolean isValidAge(int age) {
        return age >= 16 && age <= 65; // Working age in Vietnam
    }
    
    // Validate working hours
    public static boolean isValidWorkingHours(String timeStr) {
        if (isEmpty(timeStr)) return false;
        
        // HH:mm format
        if (!timeStr.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) return false;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            sdf.parse(timeStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Calculate age from birth date
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) return 0;
        
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        
        Calendar now = Calendar.getInstance();
        
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        
        return age;
    }
    
    // Clean and format phone number
    public static String formatPhoneNumber(String phone) {
        if (isEmpty(phone)) return "";
        
        // Remove all non-digit characters
        String cleaned = phone.replaceAll("[^0-9]", "");
        
        // Convert +84 to 0
        if (cleaned.startsWith("84")) {
            cleaned = "0" + cleaned.substring(2);
        }
        
        return cleaned;
    }
    
    // Sanitize string input (prevent XSS)
    public static String sanitizeString(String input) {
        if (isEmpty(input)) return "";
        
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("/", "&#x2F;");
    }
    
    // Validate Vietnamese ID card number
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) return false;
        
        // Remove spaces and dashes
        String cleaned = idCard.replaceAll("[\\s-]", "");
        
        // Old format: 9 digits or New format: 12 digits
        return cleaned.matches("^\\d{9}$") || cleaned.matches("^\\d{12}$");
    }
}
