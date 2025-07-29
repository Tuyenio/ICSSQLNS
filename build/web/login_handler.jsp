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
                NhanVien nhanVien = nhanVienDAO.login(email.trim(), matKhau.trim());
                
                if (nhanVien != null) {
                    // Kiểm tra trạng thái làm việc
                    if (!"DangLam".equals(nhanVien.getTrangThaiLamViec())) {
                        request.setAttribute("errorMsg", "Tài khoản của bạn hiện không hoạt động. Vui lòng liên hệ HR.");
                    } else {
                        // Đăng nhập thành công
                        AuthUtil.login(session, nhanVien);
                        
                        // Chuyển hướng dựa trên vai trò
                        if (AuthUtil.isAdmin(session) || AuthUtil.isManager(session)) {
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            response.sendRedirect("user_dashboard.jsp");
                            return;
                        }
                    }
                } else {
                    request.setAttribute("errorMsg", "Email hoặc mật khẩu không đúng!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Có lỗi xảy ra trong quá trình đăng nhập. Vui lòng thử lại!");
            }
        } else {
            request.setAttribute("errorMsg", "Vui lòng nhập đầy đủ email và mật khẩu!");
        }
    } else if ("logout".equals(action)) {
        // Đăng xuất
        AuthUtil.logout(session);
        request.setAttribute("successMsg", "Đăng xuất thành công!");
    }
    
    // Forward to login.jsp
    RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
    dispatcher.forward(request, response);
%>
