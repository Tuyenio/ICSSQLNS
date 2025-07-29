<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="dao.ChamCongDAO"%>
<%@page import="model.ChamCong"%>
<%@page import="util.AuthUtil"%>
<%@page import="java.sql.Date"%>

<%
    // Kiểm tra đăng nhập
    if (!AuthUtil.isLoggedIn(session)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Set encoding
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    ChamCongDAO chamCongDAO = new ChamCongDAO();
    String action = request.getParameter("action");
    String message = "";
    boolean success = false;
    
    try {
        if ("checkin".equals(action)) {
            String nhanVienIdStr = request.getParameter("nhanVienId");
            String ngayStr = request.getParameter("ngay");
            String loai = request.getParameter("loai");
            
            if (nhanVienIdStr != null && !nhanVienIdStr.isEmpty() && 
                ngayStr != null && !ngayStr.isEmpty()) {
                
                int nhanVienId = Integer.parseInt(nhanVienIdStr);
                Date ngay = Date.valueOf(ngayStr);
                
                if ("checkin".equals(loai)) {
                    success = chamCongDAO.checkIn(nhanVienId, ngay);
                    message = success ? "Check-in thành công!" : "Lỗi khi check-in!";
                } else if ("checkout".equals(loai)) {
                    success = chamCongDAO.checkOut(nhanVienId, ngay);
                    message = success ? "Check-out thành công!" : "Lỗi khi check-out!";
                }
            } else {
                message = "Vui lòng điền đầy đủ thông tin!";
                success = false;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        message = "Có lỗi xảy ra: " + e.getMessage();
        success = false;
    }
    
    // Redirect về trang chấm công
    session.setAttribute("successMsg", success ? message : null);
    session.setAttribute("errorMsg", success ? null : message);
    response.sendRedirect(request.getContextPath() + "/attendance.jsp");
%>
