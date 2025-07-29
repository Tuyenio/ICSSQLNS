<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="dao.PhongBanDAO"%>
<%@page import="model.PhongBan"%>
<%@page import="util.AuthUtil"%>

<%
    // Kiểm tra đăng nhập
    if (!AuthUtil.isLoggedIn(session)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Set encoding
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    PhongBanDAO phongBanDAO = new PhongBanDAO();
    String action = request.getParameter("action");
    String message = "";
    boolean success = false;
    
    try {
        if ("add".equals(action)) {
            // Thêm phòng ban mới
            PhongBan phongBan = new PhongBan();
            phongBan.setTenPhongBan(request.getParameter("tenPhongBan"));
            phongBan.setMoTa(request.getParameter("moTa"));
            phongBan.setTruongPhong(request.getParameter("truongPhong"));
            phongBan.setDienThoai(request.getParameter("dienThoai"));
            phongBan.setEmail(request.getParameter("email"));
            
            // Validation
            if (phongBan.getTenPhongBan() == null || phongBan.getTenPhongBan().trim().isEmpty()) {
                message = "Tên phòng ban không được để trống!";
                success = false;
            } else {
                success = phongBanDAO.addPhongBan(phongBan);
                message = success ? "Thêm phòng ban thành công!" : "Không thể thêm phòng ban!";
            }
            
        } else if ("update".equals(action)) {
            // Cập nhật phòng ban
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                PhongBan phongBan = new PhongBan();
                phongBan.setId(Integer.parseInt(idStr));
                phongBan.setTenPhongBan(request.getParameter("tenPhongBan"));
                phongBan.setMoTa(request.getParameter("moTa"));
                phongBan.setTruongPhong(request.getParameter("truongPhong"));
                phongBan.setDienThoai(request.getParameter("dienThoai"));
                phongBan.setEmail(request.getParameter("email"));
                
                // Validation
                if (phongBan.getTenPhongBan() == null || phongBan.getTenPhongBan().trim().isEmpty()) {
                    message = "Tên phòng ban không được để trống!";
                    success = false;
                } else {
                    success = phongBanDAO.updatePhongBan(phongBan);
                    message = success ? "Cập nhật phòng ban thành công!" : "Không thể cập nhật phòng ban!";
                }
            } else {
                message = "ID phòng ban không hợp lệ!";
                success = false;
            }
            
        } else if ("delete".equals(action)) {
            // Xóa phòng ban
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                success = phongBanDAO.deletePhongBan(id);
                message = success ? "Xóa phòng ban thành công!" : "Không thể xóa phòng ban!";
            } else {
                message = "ID phòng ban không hợp lệ!";
                success = false;
            }
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        success = false;
        message = "Lỗi xử lý: " + e.getMessage();
    }
    
    // Set message attributes
    if (success) {
        request.setAttribute("successMsg", message);
    } else {
        request.setAttribute("errorMsg", message);
    }
    
    // Lấy danh sách phòng ban để hiển thị
    try {
        List<PhongBan> danhSachPhongBan = phongBanDAO.getAllPhongBan();
        request.setAttribute("danhSachPhongBan", danhSachPhongBan);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMsg", "Lỗi khi lấy danh sách phòng ban: " + e.getMessage());
    }
    
    // Forward to department.jsp
    RequestDispatcher dispatcher = request.getRequestDispatcher("/department.jsp");
    dispatcher.forward(request, response);
%>
