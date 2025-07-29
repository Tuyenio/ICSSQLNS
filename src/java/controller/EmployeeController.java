package controller;

import dao.NhanVienDAO;
import dao.PhongBanDAO;
import model.NhanVien;
import model.PhongBan;
import java.sql.Date;
import java.util.List;

/**
 * Controller để xử lý các thao tác với nhân viên
 * Sử dụng JSP để gọi trực tiếp thay vì servlet
 */
public class EmployeeController {
    private NhanVienDAO nhanVienDAO;
    private PhongBanDAO phongBanDAO;
    
    public EmployeeController() {
        this.nhanVienDAO = new NhanVienDAO();
        this.phongBanDAO = new PhongBanDAO();
    }
    
    // Lấy danh sách tất cả nhân viên
    public List<NhanVien> getAllEmployees() {
        try {
            return nhanVienDAO.getAllNhanVien();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Lấy danh sách phòng ban cho dropdown
    public List<PhongBan> getDepartments() {
        try {
            return phongBanDAO.getPhongBanForDropdown();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Lấy nhân viên theo ID
    public NhanVien getEmployeeById(int id) {
        try {
            return nhanVienDAO.getNhanVienById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Thêm nhân viên mới
    public boolean addEmployee(NhanVien nhanVien) {
        try {
            // Validation
            if (nhanVien.getHoTen() == null || nhanVien.getHoTen().trim().isEmpty()) {
                return false;
            }
            
            if (nhanVien.getEmail() == null || nhanVien.getEmail().trim().isEmpty()) {
                return false;
            }
            
            if (nhanVienDAO.isEmailExists(nhanVien.getEmail(), 0)) {
                return false;
            }
            
            if (nhanVien.getMatKhau() == null || nhanVien.getMatKhau().trim().isEmpty()) {
                return false;
            }
            
            return nhanVienDAO.addNhanVien(nhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật nhân viên
    public boolean updateEmployee(NhanVien nhanVien) {
        try {
            // Validation
            if (nhanVien.getId() <= 0) {
                return false;
            }
            
            if (nhanVien.getHoTen() == null || nhanVien.getHoTen().trim().isEmpty()) {
                return false;
            }
            
            if (nhanVien.getEmail() == null || nhanVien.getEmail().trim().isEmpty()) {
                return false;
            }
            
            if (nhanVienDAO.isEmailExists(nhanVien.getEmail(), nhanVien.getId())) {
                return false;
            }
            
            return nhanVienDAO.updateNhanVien(nhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa nhân viên
    public boolean deleteEmployee(int id) {
        try {
            if (id <= 0) {
                return false;
            }
            
            return nhanVienDAO.deleteNhanVien(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tìm kiếm nhân viên
    public List<NhanVien> searchEmployees(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllEmployees();
            }
            
            return nhanVienDAO.searchNhanVien(keyword, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Kiểm tra email tồn tại
    public boolean isEmailExists(String email, int excludeId) {
        try {
            return nhanVienDAO.isEmailExists(email, excludeId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Chuyển đổi chuỗi ngày thành Date
    public Date parseDate(String dateStr) {
        try {
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                return Date.valueOf(dateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Chuyển đổi chuỗi số thành Integer
    public Integer parseInt(String intStr) {
        try {
            if (intStr != null && !intStr.trim().isEmpty()) {
                return Integer.parseInt(intStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Tạo response JSON đơn giản
    public String createJsonResponse(boolean success, String message, Object data) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(message).append("\"");
        if (data != null) {
            json.append(",\"data\":\"").append(data.toString()).append("\"");
        }
        json.append("}");
        return json.toString();
    }
    
    // Validate nhân viên
    public String validateEmployee(NhanVien nhanVien, boolean isUpdate) {
        if (nhanVien.getHoTen() == null || nhanVien.getHoTen().trim().isEmpty()) {
            return "Họ tên không được để trống!";
        }
        
        if (nhanVien.getEmail() == null || nhanVien.getEmail().trim().isEmpty()) {
            return "Email không được để trống!";
        }
        
        // Kiểm tra định dạng email
        if (!nhanVien.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
            return "Email không đúng định dạng!";
        }
        
        // Kiểm tra email trùng lặp
        int excludeId = isUpdate ? nhanVien.getId() : 0;
        if (isEmailExists(nhanVien.getEmail(), excludeId)) {
            return "Email đã tồn tại trong hệ thống!";
        }
        
        if (!isUpdate && (nhanVien.getMatKhau() == null || nhanVien.getMatKhau().trim().isEmpty())) {
            return "Mật khẩu không được để trống!";
        }
        
        // Kiểm tra số điện thoại
        if (nhanVien.getSoDienThoai() != null && !nhanVien.getSoDienThoai().isEmpty()) {
            if (!nhanVien.getSoDienThoai().matches("^[0-9]{10,11}$")) {
                return "Số điện thoại không đúng định dạng!";
            }
        }
        
        return null; // Không có lỗi
    }
    
    // Tạo đối tượng NhanVien từ parameters
    public NhanVien createEmployeeFromParams(String id, String hoTen, String email, String matKhau,
            String soDienThoai, String gioiTinh, String ngaySinh, String phongBanId,
            String chucVu, String trangThaiLamViec, String vaiTro, String ngayVaoLam,
            String avatarUrl) {
        
        NhanVien nhanVien = new NhanVien();
        
        // Set ID nếu có (cho trường hợp update)
        if (id != null && !id.trim().isEmpty()) {
            nhanVien.setId(Integer.parseInt(id));
        }
        
        nhanVien.setHoTen(hoTen != null ? hoTen.trim() : "");
        nhanVien.setEmail(email != null ? email.trim() : "");
        nhanVien.setMatKhau(matKhau != null ? matKhau.trim() : "");
        nhanVien.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
        nhanVien.setGioiTinh(gioiTinh != null ? gioiTinh.trim() : "Nam");
        nhanVien.setNgaySinh(parseDate(ngaySinh));
        
        Integer phongBanIdInt = parseInt(phongBanId);
        if (phongBanIdInt != null) {
            nhanVien.setPhongBanId(phongBanIdInt);
        }
        
        nhanVien.setChucVu(chucVu != null ? chucVu.trim() : "");
        nhanVien.setTrangThaiLamViec(trangThaiLamViec != null ? trangThaiLamViec.trim() : "Đang làm việc");
        nhanVien.setVaiTro(vaiTro != null ? vaiTro.trim() : "Nhân viên");
        nhanVien.setNgayVaoLam(parseDate(ngayVaoLam));
        nhanVien.setAvatarUrl(avatarUrl != null ? avatarUrl.trim() : "");
        
        return nhanVien;
    }
}
