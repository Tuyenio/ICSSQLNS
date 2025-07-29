package util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {
    
    // Date format patterns
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    public static final String DATETIME_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_HH_MM = "HH:mm";
    public static final String TIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    
    // Vietnamese locale
    private static final Locale VIETNAMESE_LOCALE = new Locale("vi", "VN");
    
    // Format date to string
    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, VIETNAMESE_LOCALE);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }
    
    // Format date with default pattern (dd/MM/yyyy)
    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT_DD_MM_YYYY);
    }
    
    // Format datetime with default pattern
    public static String formatDateTime(Date date) {
        return formatDate(date, DATETIME_FORMAT_DD_MM_YYYY_HH_MM);
    }
    
    // Format time
    public static String formatTime(Date date) {
        return formatDate(date, TIME_FORMAT_HH_MM);
    }
    
    // Parse string to date
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, VIETNAMESE_LOCALE);
            sdf.setLenient(false);
            return sdf.parse(dateStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }
    
    // Parse date with default pattern (dd/MM/yyyy)
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT_DD_MM_YYYY);
    }
    
    // Parse SQL date format (yyyy-MM-dd)
    public static Date parseSqlDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT_YYYY_MM_DD);
    }
    
    // Parse datetime
    public static Date parseDateTime(String dateTimeStr) {
        return parseDate(dateTimeStr, DATETIME_FORMAT_DD_MM_YYYY_HH_MM);
    }
    
    // Get current date
    public static Date getCurrentDate() {
        return new Date();
    }
    
    // Get current timestamp
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    // Get today at start of day (00:00:00)
    public static Date getTodayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    // Get today at end of day (23:59:59)
    public static Date getTodayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    // Add days to date
    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    // Add months to date
    public static Date addMonths(Date date, int months) {
        if (date == null) return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
    
    // Add years to date
    public static Date addYears(Date date, int years) {
        if (date == null) return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }
    
    // Get first day of month
    public static Date getFirstDayOfMonth(Date date) {
        if (date == null) return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    // Get last day of month
    public static Date getLastDayOfMonth(Date date) {
        if (date == null) return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    // Get first day of year
    public static Date getFirstDayOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    // Get last day of year
    public static Date getLastDayOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    // Calculate difference in days
    public static long getDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return diffInMillies / (24 * 60 * 60 * 1000);
    }
    
    // Calculate difference in months
    public static int getMonthsBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        
        int yearDiff = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);
        int monthDiff = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);
        
        return yearDiff * 12 + monthDiff;
    }
    
    // Check if date is today
    public static boolean isToday(Date date) {
        if (date == null) return false;
        
        Calendar today = Calendar.getInstance();
        Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(date);
        
        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR);
    }
    
    // Check if date is yesterday
    public static boolean isYesterday(Date date) {
        if (date == null) return false;
        
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        
        Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(date);
        
        return yesterday.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
               yesterday.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR);
    }
    
    // Check if date is in current month
    public static boolean isCurrentMonth(Date date) {
        if (date == null) return false;
        
        Calendar today = Calendar.getInstance();
        Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(date);
        
        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
               today.get(Calendar.MONTH) == checkDate.get(Calendar.MONTH);
    }
    
    // Check if date is weekend
    public static boolean isWeekend(Date date) {
        if (date == null) return false;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }
    
    // Get age from birth date
    public static int getAge(Date birthDate) {
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
    
    // Get working days between two dates (excluding weekends)
    public static int getWorkingDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        
        int workingDays = 0;
        while (!start.after(end)) {
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                workingDays++;
            }
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return workingDays;
    }
    
    // Convert Date to LocalDate
    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    // Convert LocalDate to Date
    public static Date fromLocalDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    // Convert Date to LocalDateTime
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    // Convert LocalDateTime to Date
    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    // Get Vietnamese day name
    public static String getVietnameseDayName(Date date) {
        if (date == null) return "";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Thứ Hai";
            case Calendar.TUESDAY: return "Thứ Ba";
            case Calendar.WEDNESDAY: return "Thứ Tư";
            case Calendar.THURSDAY: return "Thứ Năm";
            case Calendar.FRIDAY: return "Thứ Sáu";
            case Calendar.SATURDAY: return "Thứ Bảy";
            case Calendar.SUNDAY: return "Chủ Nhật";
            default: return "";
        }
    }
    
    // Get Vietnamese month name
    public static String getVietnameseMonthName(int month) {
        String[] monthNames = {
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
            "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
            "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };
        
        if (month >= 1 && month <= 12) {
            return monthNames[month - 1];
        }
        
        return "";
    }
    
    // Format date in Vietnamese style
    public static String formatVietnameseDate(Date date) {
        if (date == null) return "";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        String dayName = getVietnameseDayName(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String monthName = getVietnameseMonthName(cal.get(Calendar.MONTH) + 1);
        int year = cal.get(Calendar.YEAR);
        
        return String.format("%s, ngày %d %s năm %d", dayName, day, monthName, year);
    }
}
