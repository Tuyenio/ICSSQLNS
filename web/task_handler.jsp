<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="dao.CongViecDAO"%>
<%@page import="dao.NhanVienDAO"%>
<%@page import="model.CongViec"%>
<%@page import="model.NhanVien"%>
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
    
    CongViecDAO congViecDAO = new CongViecDAO();
    NhanVienDAO nhanVienDAO = new NhanVienDAO();
    String action = request.getParameter("action");
    String message = "";
    boolean success = false;
    
    try {
        if ("add".equals(action)) {
            // Thêm công việc mới
            CongViec congViec = new CongViec();
            congViec.setTenCongViec(request.getParameter("tenCongViec"));
            congViec.setMoTa(request.getParameter("moTa"));
            
            String nguoiGiaoStr = request.getParameter("nguoiGiao");
            if (nguoiGiaoStr != null && !nguoiGiaoStr.isEmpty()) {
                congViec.setNguoiGiao(Integer.parseInt(nguoiGiaoStr));
            }
            
            String nguoiNhanStr = request.getParameter("nguoiNhan");
            if (nguoiNhanStr != null && !nguoiNhanStr.isEmpty()) {
                congViec.setNguoiNhan(Integer.parseInt(nguoiNhanStr));
            }
            
            String ngayBatDauStr = request.getParameter("ngayBatDau");
            if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
                congViec.setNgayBatDau(Date.valueOf(ngayBatDauStr));
            }
            
            String ngayKetThucStr = request.getParameter("ngayKetThuc");
            if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
                congViec.setNgayKetThuc(Date.valueOf(ngayKetThucStr));
            }
            
            String trangThai = request.getParameter("trangThai");
            if (trangThai == null || trangThai.trim().isEmpty()) {
                trangThai = "ChuaBatDau"; // Giá trị mặc định
            }
            congViec.setTrangThai(trangThai);
            
            String mucDoUuTien = request.getParameter("mucDoUuTien");
            if (mucDoUuTien == null || mucDoUuTien.trim().isEmpty()) {
                mucDoUuTien = "TrungBinh"; // Giá trị mặc định
            }
            congViec.setMucDoUuTien(mucDoUuTien);
            
            // Validation
            if (congViec.getTenCongViec() == null || congViec.getTenCongViec().trim().isEmpty()) {
                message = "Tên công việc không được để trống!";
                success = false;
            } else {
                success = congViecDAO.addCongViec(congViec);
                message = success ? "Thêm công việc thành công!" : "Không thể thêm công việc!";
            }
            
        } else if ("update".equals(action)) {
            // Cập nhật công việc
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                CongViec congViec = new CongViec();
                congViec.setId(Integer.parseInt(idStr));
                congViec.setTenCongViec(request.getParameter("tenCongViec"));
                congViec.setMoTa(request.getParameter("moTa"));
                
                String nguoiGiaoStr = request.getParameter("nguoiGiao");
                if (nguoiGiaoStr != null && !nguoiGiaoStr.isEmpty()) {
                    congViec.setNguoiGiao(Integer.parseInt(nguoiGiaoStr));
                }
                
                String nguoiNhanStr = request.getParameter("nguoiNhan");
                if (nguoiNhanStr != null && !nguoiNhanStr.isEmpty()) {
                    congViec.setNguoiNhan(Integer.parseInt(nguoiNhanStr));
                }
                
                String ngayBatDauStr = request.getParameter("ngayBatDau");
                if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
                    congViec.setNgayBatDau(Date.valueOf(ngayBatDauStr));
                }
                
                String ngayKetThucStr = request.getParameter("ngayKetThuc");
                if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
                    congViec.setNgayKetThuc(Date.valueOf(ngayKetThucStr));
                }
                
                congViec.setTrangThai(request.getParameter("trangThai"));
                congViec.setMucDoUuTien(request.getParameter("mucDoUuTien"));
                
                // Validation
                if (congViec.getTenCongViec() == null || congViec.getTenCongViec().trim().isEmpty()) {
                    message = "Tên công việc không được để trống!";
                    success = false;
                } else {
                    success = congViecDAO.updateCongViec(congViec);
                    message = success ? "Cập nhật công việc thành công!" : "Không thể cập nhật công việc!";
                }
            } else {
                message = "ID công việc không hợp lệ!";
                success = false;
            }
            
        } else if ("delete".equals(action)) {
            // Xóa công việc
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                success = congViecDAO.deleteCongViec(id);
                message = success ? "Xóa công việc thành công!" : "Không thể xóa công việc!";
            } else {
                message = "ID công việc không hợp lệ!";
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
    
    // Lấy danh sách công việc để hiển thị
    try {
        List<CongViec> danhSachCongViec = congViecDAO.getAllCongViec();
        request.setAttribute("danhSachCongViec", danhSachCongViec);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMsg", "Lỗi khi lấy danh sách công việc: " + e.getMessage());
    }
    
    // Lấy danh sách nhân viên cho dropdown
    try {
        List<NhanVien> danhSachNhanVien = nhanVienDAO.getAllNhanVien();
        request.setAttribute("danhSachNhanVien", danhSachNhanVien);
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Forward to task.jsp
    RequestDispatcher dispatcher = request.getRequestDispatcher("/task.jsp");
    dispatcher.forward(request, response);
%>
