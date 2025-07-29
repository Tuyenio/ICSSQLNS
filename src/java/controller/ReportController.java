package controller;

import dao.ReportDAO;
import util.AuthUtil;
import java.util.Map;

public class ReportController {
    
    private ReportDAO reportDAO;
    
    public ReportController() {
        this.reportDAO = new ReportDAO();
    }
    
    // Xử lý yêu cầu chính
    public String processRequest(Object request, Object response, Object session) {
        try {
            // Kiểm tra đăng nhập
            if (!AuthUtil.isLoggedIn(session)) {
                return "redirect:login.jsp";
            }
            
            // Chỉ admin/manager mới xem được báo cáo
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền xem báo cáo!");
                return "error.jsp";
            }
            
            String action = getParameter(request, "action");
            
            switch (action != null ? action : "dashboard") {
                case "dashboard":
                    return showDashboard(request, session);
                case "employee_stats":
                    return showEmployeeStatistics(request, session);
                case "attendance":
                    return showAttendanceReport(request, session);
                case "salary":
                    return showSalaryReport(request, session);
                case "task":
                    return showTaskReport(request, session);
                case "export":
                    return exportReport(request, response, session);
                default:
                    return showDashboard(request, session);
            }
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi hệ thống: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Hiển thị dashboard báo cáo tổng quan
    private String showDashboard(Object request, Object session) {
        try {
            // Lấy thống kê nhân viên
            Map<String, Object> employeeStats = reportDAO.getEmployeeStatistics();
            setAttribute(request, "employeeStats", employeeStats);
            
            // Lấy tháng/năm hiện tại hoặc từ request
            String monthParam = getParameter(request, "month");
            String yearParam = getParameter(request, "year");
            
            java.time.LocalDate now = java.time.LocalDate.now();
            int month = monthParam != null ? Integer.parseInt(monthParam) : now.getMonthValue();
            int year = yearParam != null ? Integer.parseInt(yearParam) : now.getYear();
            
            // Báo cáo chấm công tháng hiện tại
            Map<String, Object> attendanceReport = reportDAO.getAttendanceReport(month, year);
            setAttribute(request, "attendanceReport", attendanceReport);
            
            // Báo cáo lương tháng hiện tại
            Map<String, Object> salaryReport = reportDAO.getSalaryReport(month, year);
            setAttribute(request, "salaryReport", salaryReport);
            
            // Báo cáo công việc tháng hiện tại
            Map<String, Object> taskReport = reportDAO.getTaskReport(month, year);
            setAttribute(request, "taskReport", taskReport);
            
            setAttribute(request, "currentMonth", month);
            setAttribute(request, "currentYear", year);
            
            return "report.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải báo cáo: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Hiển thị thống kê nhân viên chi tiết
    private String showEmployeeStatistics(Object request, Object session) {
        try {
            Map<String, Object> employeeStats = reportDAO.getEmployeeStatistics();
            setAttribute(request, "employeeStats", employeeStats);
            setAttribute(request, "reportType", "employee");
            
            return "report_employee.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải thống kê nhân viên: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Hiển thị báo cáo chấm công
    private String showAttendanceReport(Object request, Object session) {
        try {
            String monthParam = getParameter(request, "month");
            String yearParam = getParameter(request, "year");
            
            java.time.LocalDate now = java.time.LocalDate.now();
            int month = monthParam != null ? Integer.parseInt(monthParam) : now.getMonthValue();
            int year = yearParam != null ? Integer.parseInt(yearParam) : now.getYear();
            
            Map<String, Object> attendanceReport = reportDAO.getAttendanceReport(month, year);
            setAttribute(request, "attendanceReport", attendanceReport);
            setAttribute(request, "reportType", "attendance");
            setAttribute(request, "selectedMonth", month);
            setAttribute(request, "selectedYear", year);
            
            return "report_attendance.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải báo cáo chấm công: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Hiển thị báo cáo lương
    private String showSalaryReport(Object request, Object session) {
        try {
            String monthParam = getParameter(request, "month");
            String yearParam = getParameter(request, "year");
            
            java.time.LocalDate now = java.time.LocalDate.now();
            int month = monthParam != null ? Integer.parseInt(monthParam) : now.getMonthValue();
            int year = yearParam != null ? Integer.parseInt(yearParam) : now.getYear();
            
            Map<String, Object> salaryReport = reportDAO.getSalaryReport(month, year);
            setAttribute(request, "salaryReport", salaryReport);
            setAttribute(request, "reportType", "salary");
            setAttribute(request, "selectedMonth", month);
            setAttribute(request, "selectedYear", year);
            
            return "report_salary.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải báo cáo lương: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Hiển thị báo cáo công việc
    private String showTaskReport(Object request, Object session) {
        try {
            String monthParam = getParameter(request, "month");
            String yearParam = getParameter(request, "year");
            
            java.time.LocalDate now = java.time.LocalDate.now();
            int month = monthParam != null ? Integer.parseInt(monthParam) : now.getMonthValue();
            int year = yearParam != null ? Integer.parseInt(yearParam) : now.getYear();
            
            Map<String, Object> taskReport = reportDAO.getTaskReport(month, year);
            setAttribute(request, "taskReport", taskReport);
            setAttribute(request, "reportType", "task");
            setAttribute(request, "selectedMonth", month);
            setAttribute(request, "selectedYear", year);
            
            return "report_task.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải báo cáo công việc: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Xuất báo cáo (CSV, Excel, PDF)
    private String exportReport(Object request, Object response, Object session) {
        try {
            String reportType = getParameter(request, "reportType");
            String format = getParameter(request, "format"); // csv, excel, pdf
            String monthParam = getParameter(request, "month");
            String yearParam = getParameter(request, "year");
            
            java.time.LocalDate now = java.time.LocalDate.now();
            int month = monthParam != null ? Integer.parseInt(monthParam) : now.getMonthValue();
            int year = yearParam != null ? Integer.parseInt(yearParam) : now.getYear();
            
            Map<String, Object> reportData = null;
            String filename = "";
            
            switch (reportType != null ? reportType : "attendance") {
                case "employee":
                    reportData = reportDAO.getEmployeeStatistics();
                    filename = "thong_ke_nhan_vien";
                    break;
                case "attendance":
                    reportData = reportDAO.getAttendanceReport(month, year);
                    filename = "bao_cao_cham_cong_" + month + "_" + year;
                    break;
                case "salary":
                    reportData = reportDAO.getSalaryReport(month, year);
                    filename = "bao_cao_luong_" + month + "_" + year;
                    break;
                case "task":
                    reportData = reportDAO.getTaskReport(month, year);
                    filename = "bao_cao_cong_viec_" + month + "_" + year;
                    break;
                default:
                    setAttribute(request, "error", "Loại báo cáo không hợp lệ!");
                    return "report.jsp";
            }
            
            // Set response headers for download
            setResponseHeader(response, "Content-Disposition", 
                "attachment; filename=\"" + filename + "." + format + "\"");
            
            if ("csv".equals(format)) {
                setResponseHeader(response, "Content-Type", "text/csv; charset=UTF-8");
                return generateCSV(reportData, reportType);
            } else if ("excel".equals(format)) {
                setResponseHeader(response, "Content-Type", 
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                return generateExcel(reportData, reportType);
            } else if ("pdf".equals(format)) {
                setResponseHeader(response, "Content-Type", "application/pdf");
                return generatePDF(reportData, reportType);
            } else {
                setAttribute(request, "error", "Định dạng xuất không hỗ trợ!");
                return "report.jsp";
            }
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể xuất báo cáo: " + e.getMessage());
            return "report.jsp";
        }
    }
    
    // Generate CSV content
    private String generateCSV(Map<String, Object> data, String reportType) {
        StringBuilder csv = new StringBuilder();
        
        // Add UTF-8 BOM for Vietnamese characters
        csv.append("\uFEFF");
        
        switch (reportType) {
            case "attendance":
                csv.append("Họ tên,Phòng ban,Số ngày chấm,Đúng giờ,Trễ giờ,Chưa ra,Tỷ lệ chấm công\n");
                @SuppressWarnings("unchecked")
                java.util.List<Map<String, Object>> attendanceData = 
                    (java.util.List<Map<String, Object>>) data.get("attendanceData");
                if (attendanceData != null) {
                    for (Map<String, Object> record : attendanceData) {
                        csv.append(record.get("employeeName")).append(",")
                           .append(record.get("department")).append(",")
                           .append(record.get("attendanceDays")).append(",")
                           .append(record.get("onTime")).append(",")
                           .append(record.get("late")).append(",")
                           .append(record.get("notCheckedOut")).append(",")
                           .append(record.get("attendanceRate")).append("%\n");
                    }
                }
                break;
            // Add other report types as needed
        }
        
        return csv.toString();
    }
    
    // Generate Excel content (simplified - would need Apache POI in production)
    private String generateExcel(Map<String, Object> data, String reportType) {
        // This is a placeholder - would need Apache POI library for real Excel generation
        return generateCSV(data, reportType); // Fallback to CSV
    }
    
    // Generate PDF content (simplified - would need iText or similar in production)
    private String generatePDF(Map<String, Object> data, String reportType) {
        // This is a placeholder - would need PDF library for real PDF generation
        return generateCSV(data, reportType); // Fallback to CSV
    }
    
    // Helper methods để tránh dependency với servlet
    private String getParameter(Object request, String name) {
        try {
            return (String) request.getClass().getMethod("getParameter", String.class).invoke(request, name);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void setAttribute(Object request, String name, Object value) {
        try {
            request.getClass().getMethod("setAttribute", String.class, Object.class).invoke(request, name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setResponseHeader(Object response, String name, String value) {
        try {
            response.getClass().getMethod("setHeader", String.class, String.class).invoke(response, name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
