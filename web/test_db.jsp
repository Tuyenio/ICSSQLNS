<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.DatabaseConnection"%>
<%@page import="dao.NhanVienDAO"%>
<%@page import="model.NhanVien"%>
<%@page import="java.sql.Connection"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Database Connection</title>
</head>
<body>
    <h2>Test Database Connection</h2>
    
    <%
    try {
        out.println("<p>Testing database connection...</p>");
        
        // Test kết nối database
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null && !conn.isClosed()) {
            out.println("<p style='color: green;'>✓ Database connection successful!</p>");
            conn.close();
        } else {
            out.println("<p style='color: red;'>✗ Database connection failed!</p>");
        }
        
        // Test NhanVienDAO
        out.println("<p>Testing NhanVienDAO...</p>");
        NhanVienDAO dao = new NhanVienDAO();
        
        // Test login với dữ liệu mẫu
        NhanVien testUser = dao.login("minh@company.com", "password123");
        if (testUser != null) {
            out.println("<p style='color: green;'>✓ Login test successful! User: " + testUser.getHoTen() + "</p>");
        } else {
            out.println("<p style='color: orange;'>! No test user found (this is normal if database is empty)</p>");
        }
        
        // Test lấy tất cả nhân viên
        java.util.List<NhanVien> allEmployees = dao.getAllNhanVien();
        out.println("<p>Total employees found: " + allEmployees.size() + "</p>");
        
        out.println("<p style='color: green;'>✓ All tests completed!</p>");
        
    } catch (Exception e) {
        out.println("<p style='color: red;'>✗ Error: " + e.getMessage() + "</p>");
        e.printStackTrace();
    }
    %>
    
    <p><a href="login.jsp">Back to Login</a></p>
</body>
</html>
