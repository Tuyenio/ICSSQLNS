<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.NhanVienDAO"%>
<%@page import="model.NhanVien"%>
<%@page import="util.AuthUtil"%>

<%
    // Set encoding
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    String action = request.getParameter("action");
    
    if ("login".equals(action)) {
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        
        if (email != null && matKhau != null && !email.trim().isEmpty() && !matKhau.trim().isEmpty()) {
            try {
                NhanVienDAO nhanVienDAO = new NhanVienDAO();
                NhanVien nhanVien = nhanVienDAO.login(email, matKhau);
                
                if (nhanVien != null) {
                    // Đăng nhập thành công
                    AuthUtil.login(session, nhanVien);
                    
                    // Redirect dựa trên vai trò
                    if ("admin".equals(nhanVien.getVaiTro())) {
                        response.sendRedirect(request.getContextPath() + "/employee.jsp");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/user_dashboard.jsp");
                    }
                    return;
                } else {
                    // Đăng nhập thất bại
                    request.setAttribute("errorMsg", "Email hoặc mật khẩu không đúng!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
            }
        } else {
            request.setAttribute("errorMsg", "Vui lòng nhập đầy đủ thông tin!");
        }
    } else if ("logout".equals(action)) {
        // Đăng xuất
        AuthUtil.logout(session);
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Forward to login.jsp
    RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
    dispatcher.forward(request, response);
%>
