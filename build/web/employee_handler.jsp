<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="controller.EmployeeController"%>
<%@page import="model.NhanVien"%>
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
    
    EmployeeController controller = new EmployeeController();
    String action = request.getParameter("action");
    String message = "";
    boolean success = false;
    
    try {
        if ("add".equals(action)) {
            // Thêm nhân viên mới
            NhanVien nhanVien = controller.createEmployeeFromParams(
                null,
                request.getParameter("hoTen"),
                request.getParameter("email"),
                request.getParameter("matKhau"),
                request.getParameter("soDienThoai"),
                request.getParameter("gioiTinh"),
                request.getParameter("ngaySinh"),
                request.getParameter("phongBanId"),
                request.getParameter("chucVu"),
                request.getParameter("trangThaiLamViec"),
                request.getParameter("vaiTro"),
                request.getParameter("ngayVaoLam"),
                request.getParameter("avatarUrl")
            );
            
            // Validate
            String validationError = controller.validateEmployee(nhanVien, false);
            if (validationError != null) {
                message = validationError;
                success = false;
            } else {
                success = controller.addEmployee(nhanVien);
                message = success ? "Thêm nhân viên thành công!" : "Không thể thêm nhân viên!";
            }
            
        } else if ("update".equals(action)) {
            // Cập nhật nhân viên
            NhanVien nhanVien = controller.createEmployeeFromParams(
                request.getParameter("id"),
                request.getParameter("hoTen"),
                request.getParameter("email"),
                null, // không cập nhật mật khẩu
                request.getParameter("soDienThoai"),
                request.getParameter("gioiTinh"),
                request.getParameter("ngaySinh"),
                request.getParameter("phongBanId"),
                request.getParameter("chucVu"),
                request.getParameter("trangThaiLamViec"),
                request.getParameter("vaiTro"),
                request.getParameter("ngayVaoLam"),
                request.getParameter("avatarUrl")
            );
            
            // Validate
            String validationError = controller.validateEmployee(nhanVien, true);
            if (validationError != null) {
                message = validationError;
                success = false;
            } else {
                success = controller.updateEmployee(nhanVien);
                message = success ? "Cập nhật nhân viên thành công!" : "Không thể cập nhật nhân viên!";
            }
            
        } else if ("delete".equals(action)) {
            // Xóa nhân viên
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                success = controller.deleteEmployee(id);
                message = success ? "Xóa nhân viên thành công!" : "Không thể xóa nhân viên!";
            } else {
                message = "ID nhân viên không hợp lệ!";
                success = false;
            }
            
        } else if ("search".equals(action)) {
            // Tìm kiếm nhân viên
            String keyword = request.getParameter("keyword");
            List<NhanVien> searchResults = controller.searchEmployees(keyword);
            
            if (searchResults != null) {
                request.setAttribute("danhSach", searchResults);
                success = true;
                message = "Tìm kiếm thành công! Tìm thấy " + searchResults.size() + " kết quả.";
            } else {
                message = "Lỗi khi tìm kiếm!";
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
    
    // Lấy danh sách nhân viên để hiển thị
    if (request.getAttribute("danhSach") == null) {
        List<NhanVien> danhSach = controller.getAllEmployees();
        request.setAttribute("danhSach", danhSach);
    }
    
    // Lấy danh sách phòng ban cho dropdown
    List<PhongBan> danhSachPhongBan = controller.getDepartments();
    request.setAttribute("danhSachPhongBan", danhSachPhongBan);
    
    // Forward to employee.jsp
    RequestDispatcher dispatcher = request.getRequestDispatcher("/employee.jsp");
    dispatcher.forward(request, response);
%>
