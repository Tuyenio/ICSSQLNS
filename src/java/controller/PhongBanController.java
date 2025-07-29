package controller;

import dao.PhongBanDAO;
import dao.NhanVienDAO;
import model.PhongBan;
import model.NhanVien;
import java.util.List;

/**
 * Controller để xử lý các thao tác với phòng ban
 */
public class PhongBanController {
    private PhongBanDAO phongBanDAO;
    private NhanVienDAO nhanVienDAO;
    
    public PhongBanController() {
        this.phongBanDAO = new PhongBanDAO();
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    // Lấy tất cả phòng ban
    public List<PhongBan> getAllPhongBan() {
        return phongBanDAO.getAllPhongBan();
    }
    
    // Lấy phòng ban theo ID
    public PhongBan getPhongBanById(int id) {
        return phongBanDAO.getPhongBanById(id);
    }
    
    // Thêm phòng ban mới
    public boolean addPhongBan(String tenPhong, int truongPhongId) {
        // Validate dữ liệu
        if (tenPhong == null || tenPhong.trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra trùng tên
        if (phongBanDAO.isPhongBanExists(tenPhong.trim())) {
            return false;
        }
        
        // Kiểm tra trưởng phòng có tồn tại không
        if (truongPhongId > 0) {
            NhanVien truongPhong = nhanVienDAO.getNhanVienById(truongPhongId);
            if (truongPhong == null) {
                return false;
            }
        }
        
        PhongBan phongBan = new PhongBan();
        phongBan.setTenPhong(tenPhong.trim());
        if (truongPhongId > 0) {
            phongBan.setTruongPhongId(truongPhongId);
        }
        
        return phongBanDAO.addPhongBan(phongBan);
    }
    
    // Cập nhật phòng ban
    public boolean updatePhongBan(int id, String tenPhong, int truongPhongId) {
        // Validate dữ liệu
        if (tenPhong == null || tenPhong.trim().isEmpty()) {
            return false;
        }
        
        PhongBan phongBan = phongBanDAO.getPhongBanById(id);
        if (phongBan == null) {
            return false;
        }
        
        // Kiểm tra trùng tên (trừ chính nó)
        if (phongBanDAO.isPhongBanExistsExcludeId(tenPhong.trim(), id)) {
            return false;
        }
        
        // Kiểm tra trưởng phòng có tồn tại không
        if (truongPhongId > 0) {
            NhanVien truongPhong = nhanVienDAO.getNhanVienById(truongPhongId);
            if (truongPhong == null) {
                return false;
            }
        }
        
        phongBan.setTenPhong(tenPhong.trim());
        phongBan.setTruongPhongId(truongPhongId);
        
        return phongBanDAO.updatePhongBan(phongBan);
    }
    
    // Xóa phòng ban
    public boolean deletePhongBan(int id) {
        PhongBan phongBan = phongBanDAO.getPhongBanById(id);
        if (phongBan == null) {
            return false;
        }
        
        // Kiểm tra có nhân viên trong phòng ban không
        List<NhanVien> nhanVienTrongPhong = nhanVienDAO.getAllNhanVien()
            .stream()
            .filter(nv -> nv.getPhongBanId() == id)
            .toList();
            
        if (!nhanVienTrongPhong.isEmpty()) {
            return false; // Không thể xóa vì còn nhân viên
        }
        
        return phongBanDAO.deletePhongBan(id);
    }
    
    // Lấy danh sách nhân viên theo phòng ban
    public List<NhanVien> getNhanVienByPhongBan(int phongBanId) {
        return nhanVienDAO.getAllNhanVien()
            .stream()
            .filter(nv -> nv.getPhongBanId() == phongBanId)
            .toList();
    }
    
    // Thống kê số nhân viên theo phòng ban
    public int countNhanVienByPhongBan(int phongBanId) {
        return getNhanVienByPhongBan(phongBanId).size();
    }
    
    // Lấy danh sách phòng ban để hiển thị trong dropdown
    public List<PhongBan> getPhongBanForDropdown() {
        return phongBanDAO.getPhongBanForDropdown();
    }
    
    // Lấy danh sách nhân viên có thể làm trưởng phòng
    public List<NhanVien> getNhanVienForTruongPhong() {
        return nhanVienDAO.getAllNhanVien()
            .stream()
            .filter(nv -> "DangLam".equals(nv.getTrangThaiLamViec()))
            .filter(nv -> "quanly".equals(nv.getVaiTro()) || "admin".equals(nv.getVaiTro()))
            .toList();
    }
    
    // Validate dữ liệu phòng ban
    public String validatePhongBan(String tenPhong, int truongPhongId, int excludeId) {
        if (tenPhong == null || tenPhong.trim().isEmpty()) {
            return "Tên phòng ban không được để trống";
        }
        
        if (tenPhong.trim().length() > 100) {
            return "Tên phòng ban không được quá 100 ký tự";
        }
        
        // Kiểm tra trùng tên
        boolean exists = excludeId > 0 ? 
            phongBanDAO.isPhongBanExistsExcludeId(tenPhong.trim(), excludeId) :
            phongBanDAO.isPhongBanExists(tenPhong.trim());
            
        if (exists) {
            return "Tên phòng ban đã tồn tại";
        }
        
        // Kiểm tra trưởng phòng
        if (truongPhongId > 0) {
            NhanVien truongPhong = nhanVienDAO.getNhanVienById(truongPhongId);
            if (truongPhong == null) {
                return "Trưởng phòng không tồn tại";
            }
            
            if (!"DangLam".equals(truongPhong.getTrangThaiLamViec())) {
                return "Trưởng phòng phải đang làm việc";
            }
            
            if (!"quanly".equals(truongPhong.getVaiTro()) && !"admin".equals(truongPhong.getVaiTro())) {
                return "Trưởng phòng phải có vai trò quản lý hoặc admin";
            }
        }
        
        return null; // Không có lỗi
    }
}
