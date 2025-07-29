package dao;

import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {
    
    // Báo cáo thống kê nhân viên theo phòng ban
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Tổng số nhân viên
        String sqlTotal = "SELECT COUNT(*) as total FROM nhanvien WHERE trang_thai = 'Hoat_dong'";
        
        // Nhân viên theo phòng ban
        String sqlByDept = "SELECT pb.ten_phong, COUNT(nv.id) as so_luong " +
                          "FROM phong_ban pb " +
                          "LEFT JOIN nhanvien nv ON pb.id = nv.phong_ban_id AND nv.trang_thai = 'Hoat_dong' " +
                          "GROUP BY pb.id, pb.ten_phong " +
                          "ORDER BY so_luong DESC";
        
        // Nhân viên theo vai trò
        String sqlByRole = "SELECT vai_tro, COUNT(*) as so_luong " +
                          "FROM nhanvien " +
                          "WHERE trang_thai = 'Hoat_dong' " +
                          "GROUP BY vai_tro";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Tổng số nhân viên
            try (PreparedStatement stmt = conn.prepareStatement(sqlTotal);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalEmployees", rs.getInt("total"));
                }
            }
            
            // Nhân viên theo phòng ban
            List<Map<String, Object>> deptStats = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlByDept);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dept = new HashMap<>();
                    dept.put("department", rs.getString("ten_phong"));
                    dept.put("count", rs.getInt("so_luong"));
                    deptStats.add(dept);
                }
            }
            stats.put("departmentStats", deptStats);
            
            // Nhân viên theo vai trò
            List<Map<String, Object>> roleStats = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlByRole);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> role = new HashMap<>();
                    role.put("role", rs.getString("vai_tro"));
                    role.put("count", rs.getInt("so_luong"));
                    roleStats.add(role);
                }
            }
            stats.put("roleStats", roleStats);
            
        } catch (SQLException e) {
            System.err.println("Get employee statistics error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
    
    // Báo cáo chấm công theo tháng
    public Map<String, Object> getAttendanceReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT nv.ho_ten, pb.ten_phong, " +
                    "COUNT(cc.id) as so_ngay_cham, " +
                    "COUNT(CASE WHEN cc.gio_vao <= '08:00:00' THEN 1 END) as dung_gio, " +
                    "COUNT(CASE WHEN cc.gio_vao > '08:00:00' THEN 1 END) as tre_gio, " +
                    "COUNT(CASE WHEN cc.gio_ra IS NULL THEN 1 END) as chua_ra " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "LEFT JOIN cham_cong cc ON nv.id = cc.nhan_vien_id " +
                    "  AND MONTH(cc.ngay) = ? AND YEAR(cc.ngay) = ? " +
                    "WHERE nv.trang_thai = 'Hoat_dong' " +
                    "GROUP BY nv.id, nv.ho_ten, pb.ten_phong " +
                    "ORDER BY pb.ten_phong, nv.ho_ten";
        
        List<Map<String, Object>> attendanceData = new ArrayList<>();
        int totalWorkingDays = getWorkingDaysInMonth(month, year);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("employeeName", rs.getString("ho_ten"));
                    record.put("department", rs.getString("ten_phong"));
                    record.put("attendanceDays", rs.getInt("so_ngay_cham"));
                    record.put("onTime", rs.getInt("dung_gio"));
                    record.put("late", rs.getInt("tre_gio"));
                    record.put("notCheckedOut", rs.getInt("chua_ra"));
                    record.put("attendanceRate", 
                        totalWorkingDays > 0 ? 
                        Math.round((rs.getInt("so_ngay_cham") * 100.0 / totalWorkingDays) * 100.0) / 100.0 : 0);
                    attendanceData.add(record);
                }
            }
        } catch (SQLException e) {
            System.err.println("Get attendance report error: " + e.getMessage());
            e.printStackTrace();
        }
        
        report.put("month", month);
        report.put("year", year);
        report.put("totalWorkingDays", totalWorkingDays);
        report.put("attendanceData", attendanceData);
        
        return report;
    }
    
    // Báo cáo lương theo tháng
    public Map<String, Object> getSalaryReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT nv.ho_ten, pb.ten_phong, l.luong_co_ban, l.phu_cap, " +
                    "l.thuong, l.phat, l.bao_hiem, l.thue, l.luong_thuc_te, l.trang_thai " +
                    "FROM luong l " +
                    "JOIN nhanvien nv ON l.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE l.thang = ? AND l.nam = ? " +
                    "ORDER BY pb.ten_phong, nv.ho_ten";
        
        String sqlSummary = "SELECT " +
                           "COUNT(*) as so_nhan_vien, " +
                           "SUM(luong_thuc_te) as tong_luong, " +
                           "AVG(luong_thuc_te) as luong_trung_binh, " +
                           "MIN(luong_thuc_te) as luong_thap_nhat, " +
                           "MAX(luong_thuc_te) as luong_cao_nhat " +
                           "FROM luong WHERE thang = ? AND nam = ?";
        
        List<Map<String, Object>> salaryData = new ArrayList<>();
        Map<String, Object> summary = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Chi tiết lương
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> record = new HashMap<>();
                        record.put("employeeName", rs.getString("ho_ten"));
                        record.put("department", rs.getString("ten_phong"));
                        record.put("basicSalary", rs.getBigDecimal("luong_co_ban"));
                        record.put("allowance", rs.getBigDecimal("phu_cap"));
                        record.put("bonus", rs.getBigDecimal("thuong"));
                        record.put("penalty", rs.getBigDecimal("phat"));
                        record.put("insurance", rs.getBigDecimal("bao_hiem"));
                        record.put("tax", rs.getBigDecimal("thue"));
                        record.put("netSalary", rs.getBigDecimal("luong_thuc_te"));
                        record.put("status", rs.getString("trang_thai"));
                        salaryData.add(record);
                    }
                }
            }
            
            // Tóm tắt
            try (PreparedStatement stmt = conn.prepareStatement(sqlSummary)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        summary.put("employeeCount", rs.getInt("so_nhan_vien"));
                        summary.put("totalSalary", rs.getBigDecimal("tong_luong"));
                        summary.put("averageSalary", rs.getBigDecimal("luong_trung_binh"));
                        summary.put("minSalary", rs.getBigDecimal("luong_thap_nhat"));
                        summary.put("maxSalary", rs.getBigDecimal("luong_cao_nhat"));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Get salary report error: " + e.getMessage());
            e.printStackTrace();
        }
        
        report.put("month", month);
        report.put("year", year);
        report.put("salaryData", salaryData);
        report.put("summary", summary);
        
        return report;
    }
    
    // Báo cáo công việc theo trạng thái
    public Map<String, Object> getTaskReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT cv.ten_cong_viec, cv.mo_ta, nv.ho_ten as nguoi_thuc_hien, " +
                    "pb.ten_phong, cv.ngay_bat_dau, cv.ngay_ket_thuc, cv.trang_thai, cv.muc_do_uu_tien " +
                    "FROM cong_viec cv " +
                    "LEFT JOIN nhanvien nv ON cv.nguoi_thuc_hien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE (MONTH(cv.ngay_bat_dau) = ? AND YEAR(cv.ngay_bat_dau) = ?) " +
                    "   OR (MONTH(cv.ngay_ket_thuc) = ? AND YEAR(cv.ngay_ket_thuc) = ?) " +
                    "ORDER BY cv.ngay_bat_dau DESC";
        
        String sqlStats = "SELECT trang_thai, COUNT(*) as so_luong " +
                         "FROM cong_viec " +
                         "WHERE (MONTH(ngay_bat_dau) = ? AND YEAR(ngay_bat_dau) = ?) " +
                         "   OR (MONTH(ngay_ket_thuc) = ? AND YEAR(ngay_ket_thuc) = ?) " +
                         "GROUP BY trang_thai";
        
        List<Map<String, Object>> taskData = new ArrayList<>();
        List<Map<String, Object>> statusStats = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Chi tiết công việc
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                stmt.setInt(3, month);
                stmt.setInt(4, year);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> record = new HashMap<>();
                        record.put("taskName", rs.getString("ten_cong_viec"));
                        record.put("description", rs.getString("mo_ta"));
                        record.put("assignee", rs.getString("nguoi_thuc_hien"));
                        record.put("department", rs.getString("ten_phong"));
                        record.put("startDate", rs.getDate("ngay_bat_dau"));
                        record.put("endDate", rs.getDate("ngay_ket_thuc"));
                        record.put("status", rs.getString("trang_thai"));
                        record.put("priority", rs.getString("muc_do_uu_tien"));
                        taskData.add(record);
                    }
                }
            }
            
            // Thống kê theo trạng thái
            try (PreparedStatement stmt = conn.prepareStatement(sqlStats)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                stmt.setInt(3, month);
                stmt.setInt(4, year);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> stat = new HashMap<>();
                        stat.put("status", rs.getString("trang_thai"));
                        stat.put("count", rs.getInt("so_luong"));
                        statusStats.add(stat);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Get task report error: " + e.getMessage());
            e.printStackTrace();
        }
        
        report.put("month", month);
        report.put("year", year);
        report.put("taskData", taskData);
        report.put("statusStats", statusStats);
        
        return report;
    }
    
    // Tính số ngày làm việc trong tháng (trừ thứ 7, chủ nhật)
    private int getWorkingDaysInMonth(int month, int year) {
        try {
            String sql = "SELECT COUNT(*) as working_days " +
                        "FROM (SELECT DATE(?) + INTERVAL (a.i + b.i*10 + c.i*100) DAY as date " +
                        "      FROM (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
                        "            UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a " +
                        "      CROSS JOIN (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3) b " +
                        "      CROSS JOIN (SELECT 0 i UNION SELECT 1) c) dates " +
                        "WHERE MONTH(dates.date) = ? AND YEAR(dates.date) = ? " +
                        "  AND WEEKDAY(dates.date) < 5"; // 0=Monday, 4=Friday
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                String firstDay = year + "-" + String.format("%02d", month) + "-01";
                stmt.setString(1, firstDay);
                stmt.setInt(2, month);
                stmt.setInt(3, year);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("working_days");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Calculate working days error: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback: estimate 22 working days per month
        return 22;
    }
}
