package controller;

import dao.LuongDAO;
import dao.NhanVienDAO;
import model.Luong;
import model.NhanVien;
import util.AuthUtil;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Controller để xử lý các thao tác với lương
 */
public class LuongController {
    private LuongDAO luongDAO;
    private NhanVienDAO nhanVienDAO;
    
    public LuongController() {
        this.luongDAO = new LuongDAO();
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    // Xử lý yêu cầu chính
    public String processRequest(Object request, Object response, Object session) {
        try {
            // Kiểm tra đăng nhập
            if (!AuthUtil.isLoggedIn(session)) {
                return "redirect:login.jsp";
            }
            
            String action = getParameter(request, "action");
            
            if (action == null || action.isEmpty()) {
                return listLuong(request, session);
            }
            
            switch (action) {
                case "list":
                    return listLuong(request, session);
                case "add":
                    return showAddForm(request, session);
                case "edit":
                    return showEditForm(request, session);
                case "save":
                    return saveLuong(request, session);
                case "delete":
                    return deleteLuong(request, session);
                case "calculate":
                    return calculateSalary(request, session);
                case "export":
                    return exportSalary(request, session);
                case "approve":
                    return approveSalary(request, session);
                case "reject":
                    return rejectSalary(request, session);
                case "statistics":
                    return showStatistics(request, session);
                default:
                    return listLuong(request, session);
            }
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi xử lý: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Hiển thị danh sách lương
    private String listLuong(Object request, Object session) {
        try {
            List<Luong> dsLuong = luongDAO.getAllLuong();
            setAttribute(request, "dsLuong", dsLuong);
            setAttribute(request, "action", "list");
            return "salary_list.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi khi lấy danh sách lương: " + e.getMessage());
            return "salary_list.jsp";
        }
    }
    
    // Hiển thị form thêm lương
    private String showAddForm(Object request, Object session) {
        try {
            List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();
            setAttribute(request, "dsNhanVien", dsNhanVien);
            setAttribute(request, "action", "add");
            return "salary_form.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi khi hiển thị form: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Hiển thị form sửa lương
    private String showEditForm(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            Luong luong = luongDAO.getLuongById(id);
            
            if (luong == null) {
                setAttribute(request, "error", "Không tìm thấy thông tin lương!");
                return listLuong(request, session);
            }
            
            List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();
            setAttribute(request, "dsNhanVien", dsNhanVien);
            setAttribute(request, "luong", luong);
            setAttribute(request, "action", "edit");
            return "salary_form.jsp";
            
        } catch (NumberFormatException e) {
            setAttribute(request, "error", "ID không hợp lệ!");
            return listLuong(request, session);
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Lưu thông tin lương
    private String saveLuong(Object request, Object session) {
        try {
            String action = getParameter(request, "action");
            
            // Tạo đối tượng lương từ form
            Luong luong = createLuongFromRequest(request);
            
            // Validate dữ liệu
            String validationError = validateLuong(luong);
            if (validationError != null) {
                setAttribute(request, "error", validationError);
                setAttribute(request, "luong", luong);
                List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();
                setAttribute(request, "dsNhanVien", dsNhanVien);
                return "salary_form.jsp";
            }
            
            boolean success;
            if ("add".equals(action)) {
                success = luongDAO.addLuong(luong);
                if (success) {
                    setAttribute(request, "success", "Thêm lương thành công!");
                } else {
                    setAttribute(request, "error", "Lỗi khi thêm lương!");
                }
            } else {
                success = luongDAO.updateLuong(luong);
                if (success) {
                    setAttribute(request, "success", "Cập nhật lương thành công!");
                } else {
                    setAttribute(request, "error", "Lỗi khi cập nhật lương!");
                }
            }
            
            return listLuong(request, session);
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Xóa lương
    private String deleteLuong(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            
            boolean success = luongDAO.deleteLuong(id);
            if (success) {
                setAttribute(request, "success", "Xóa lương thành công!");
            } else {
                setAttribute(request, "error", "Lỗi khi xóa lương!");
            }
            
            return listLuong(request, session);
            
        } catch (NumberFormatException e) {
            setAttribute(request, "error", "ID không hợp lệ!");
            return listLuong(request, session);
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Tính lương
    private String calculateSalary(Object request, Object session) {
        try {
            int nhanVienId = Integer.parseInt(getParameter(request, "nhanVienId"));
            int thang = Integer.parseInt(getParameter(request, "thang"));
            int nam = Integer.parseInt(getParameter(request, "nam"));
            
            // Tính lương tự động
            BigDecimal luongTinhToan = luongDAO.calculateSalary(nhanVienId, thang, nam);
            
            setAttribute(request, "luongTinhToan", luongTinhToan);
            setAttribute(request, "nhanVienId", nhanVienId);
            setAttribute(request, "thang", thang);
            setAttribute(request, "nam", nam);
            
            return "salary_calculation.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi khi tính lương: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Xuất báo cáo lương
    private String exportSalary(Object request, Object session) {
        try {
            int thang = Integer.parseInt(getParameter(request, "thang"));
            int nam = Integer.parseInt(getParameter(request, "nam"));
            
            List<Luong> dsLuong = luongDAO.getLuongByThangNam(thang, nam);
            
            // Tạo báo cáo (có thể xuất Excel, PDF...)
            setAttribute(request, "dsLuong", dsLuong);
            setAttribute(request, "thang", thang);
            setAttribute(request, "nam", nam);
            
            return "salary_report.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi khi xuất báo cáo: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Duyệt lương
    private String approveSalary(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            
            boolean success = luongDAO.approveLuong(id);
            if (success) {
                setAttribute(request, "success", "Duyệt lương thành công!");
            } else {
                setAttribute(request, "error", "Lỗi khi duyệt lương!");
            }
            
            return listLuong(request, session);
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Từ chối lương
    private String rejectSalary(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            String lyDo = getParameter(request, "lyDo");
            
            boolean success = luongDAO.rejectLuong(id, lyDo);
            if (success) {
                setAttribute(request, "success", "Từ chối lương thành công!");
            } else {
                setAttribute(request, "error", "Lỗi khi từ chối lương!");
            }
            
            return listLuong(request, session);
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Hiển thị thống kê lương
    private String showStatistics(Object request, Object session) {
        try {
            // Lấy thống kê lương
            BigDecimal tongLuong = luongDAO.getTongLuongTheoThang(new Date());
            BigDecimal luongTrungBinh = luongDAO.getLuongTrungBinh();
            int soNhanVienDuocTraLuong = luongDAO.getSoNhanVienDuocTraLuong(new Date());
            
            setAttribute(request, "tongLuong", tongLuong);
            setAttribute(request, "luongTrungBinh", luongTrungBinh);
            setAttribute(request, "soNhanVienDuocTraLuong", soNhanVienDuocTraLuong);
            
            return "salary_statistics.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi khi lấy thống kê: " + e.getMessage());
            return listLuong(request, session);
        }
    }
    
    // Tạo đối tượng Luong từ request
    private Luong createLuongFromRequest(Object request) {
        Luong luong = new Luong();
        
        String idStr = getParameter(request, "id");
        if (idStr != null && !idStr.isEmpty()) {
            luong.setId(Integer.parseInt(idStr));
        }
        
        luong.setNhanVienId(Integer.parseInt(getParameter(request, "nhanVienId")));
        luong.setThang(Integer.parseInt(getParameter(request, "thang")));
        luong.setNam(Integer.parseInt(getParameter(request, "nam")));
        luong.setLuongCoBan(new BigDecimal(getParameter(request, "luongCoBan")));
        
        String phuCapStr = getParameter(request, "phuCap");
        if (phuCapStr != null && !phuCapStr.isEmpty()) {
            luong.setPhuCap(new BigDecimal(phuCapStr));
        }
        
        String thuongStr = getParameter(request, "thuong");
        if (thuongStr != null && !thuongStr.isEmpty()) {
            luong.setThuong(new BigDecimal(thuongStr));
        }
        
        String khauTruStr = getParameter(request, "khauTru");
        if (khauTruStr != null && !khauTruStr.isEmpty()) {
            luong.setKhauTru(new BigDecimal(khauTruStr));
        }
        
        String ghiChu = getParameter(request, "ghiChu");
        if (ghiChu != null) {
            luong.setGhiChu(ghiChu);
        }
        
        return luong;
    }
    
    // Validate dữ liệu lương
    private String validateLuong(Luong luong) {
        if (luong.getNhanVienId() <= 0) {
            return "Vui lòng chọn nhân viên!";
        }
        
        if (luong.getThang() < 1 || luong.getThang() > 12) {
            return "Tháng không hợp lệ!";
        }
        
        if (luong.getNam() < 2000 || luong.getNam() > 2100) {
            return "Năm không hợp lệ!";
        }
        
        if (luong.getLuongCoBan() == null || luong.getLuongCoBan().compareTo(BigDecimal.ZERO) <= 0) {
            return "Lương cơ bản phải lớn hơn 0!";
        }
        
        return null; // Hợp lệ
    }
    
    // Utility methods
    private String getParameter(Object request, String name) {
        // Implement based on your request object type
        return AuthUtil.getParameter(request, name);
    }
    
    private void setAttribute(Object request, String name, Object value) {
        // Implement based on your request object type
        AuthUtil.setAttribute(request, name, value);
    }
}
