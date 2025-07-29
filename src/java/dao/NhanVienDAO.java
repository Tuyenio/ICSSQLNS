package dao;

import model.NhanVien;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class NhanVienDAO {
    
    // Đăng nhập
    public NhanVien login(String email, String matKhau) {
        String sql = "SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE nv.email = ? AND nv.mat_khau = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNhanVien(rs);
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy danh sách tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "ORDER BY nv.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get all employees error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy nhân viên theo ID
    public NhanVien getNhanVienById(int id) {
        String sql = "SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE nv.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNhanVien(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get employee by ID error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nhanVien) {
        String sql = "INSERT INTO nhanvien (ho_ten, email, mat_khau, so_dien_thoai, " +
                    "ngay_sinh, gioi_tinh, chuc_vu, phong_ban_id, ngay_vao_lam, luong_co_ban, " +
                    "trang_thai_lam_viec, vai_tro, avatar_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, nhanVien.getHoTen());
            ps.setString(2, nhanVien.getEmail());
            ps.setString(3, nhanVien.getMatKhau());
            ps.setString(4, nhanVien.getSoDienThoai());
            ps.setDate(5, nhanVien.getNgaySinh());
            ps.setString(6, nhanVien.getGioiTinh());
            ps.setString(7, nhanVien.getChucVu());
            ps.setInt(8, nhanVien.getPhongBanId());
            ps.setDate(9, nhanVien.getNgayVaoLam());
            ps.setBigDecimal(10, nhanVien.getLuongCoBan());
            ps.setString(11, nhanVien.getTrangThaiLamViec());
            ps.setString(12, nhanVien.getVaiTro());
            ps.setString(13, nhanVien.getAvatarUrl());
            
            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    nhanVien.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Add employee error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật nhân viên
    public boolean updateNhanVien(NhanVien nhanVien) {
        String sql = "UPDATE nhanvien SET ho_ten = ?, email = ?, so_dien_thoai = ?, " +
                    "ngay_sinh = ?, gioi_tinh = ?, chuc_vu = ?, phong_ban_id = ?, ngay_vao_lam = ?, " +
                    "luong_co_ban = ?, trang_thai_lam_viec = ?, vai_tro = ?, avatar_url = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nhanVien.getHoTen());
            ps.setString(2, nhanVien.getEmail());
            ps.setString(3, nhanVien.getSoDienThoai());
            ps.setDate(4, nhanVien.getNgaySinh());
            ps.setString(5, nhanVien.getGioiTinh());
            ps.setString(6, nhanVien.getChucVu());
            ps.setInt(7, nhanVien.getPhongBanId());
            ps.setDate(8, nhanVien.getNgayVaoLam());
            ps.setBigDecimal(9, nhanVien.getLuongCoBan());
            ps.setString(10, nhanVien.getTrangThaiLamViec());
            ps.setString(11, nhanVien.getVaiTro());
            ps.setString(12, nhanVien.getAvatarUrl());
            ps.setInt(13, nhanVien.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update employee error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa nhân viên
    public boolean deleteNhanVien(int id) {
        String sql = "DELETE FROM nhanvien WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete employee error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra email tồn tại (với loại trừ ID)
    public boolean isEmailExists(String email, int excludeId) {
        String sql = "SELECT COUNT(*) FROM nhanvien WHERE email = ? AND id != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Check email exists error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra email tồn tại
    public boolean emailExists(String email) {
        return isEmailExists(email, 0);
    }
    
    // Tìm kiếm nhân viên
    public List<NhanVien> searchNhanVien(String keyword, String phongBan, String trangThai) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id WHERE 1=1");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (nv.ho_ten LIKE ? OR nv.email LIKE ? OR nv.chuc_vu LIKE ?)");
        }
        if (phongBan != null && !phongBan.trim().isEmpty()) {
            sql.append(" AND pb.ten_phong LIKE ?");
        }
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            sql.append(" AND nv.trang_thai_lam_viec = ?");
        }
        sql.append(" ORDER BY nv.id DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchTerm = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
            }
            if (phongBan != null && !phongBan.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + phongBan.trim() + "%");
            }
            if (trangThai != null && !trangThai.trim().isEmpty()) {
                ps.setString(paramIndex++, trangThai.trim());
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            System.err.println("Search employees error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Đổi mật khẩu
    public boolean changePassword(int id, String newPassword) {
        String sql = "UPDATE nhanvien SET mat_khau = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPassword);
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Change password error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Map ResultSet to NhanVien object
    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setId(rs.getInt("id"));
        nv.setHoTen(rs.getString("ho_ten"));
        nv.setEmail(rs.getString("email"));
        nv.setMatKhau(rs.getString("mat_khau"));
        nv.setSoDienThoai(rs.getString("so_dien_thoai"));
        nv.setNgaySinh(rs.getDate("ngay_sinh"));
        nv.setGioiTinh(rs.getString("gioi_tinh"));
        nv.setChucVu(rs.getString("chuc_vu"));
        nv.setPhongBanId(rs.getInt("phong_ban_id"));
        nv.setNgayVaoLam(rs.getDate("ngay_vao_lam"));
        
        BigDecimal luongCoBan = rs.getBigDecimal("luong_co_ban");
        if (luongCoBan != null) {
            nv.setLuongCoBan(luongCoBan);
        }
        
        nv.setTrangThaiLamViec(rs.getString("trang_thai_lam_viec"));
        nv.setVaiTro(rs.getString("vai_tro"));
        nv.setAvatarUrl(rs.getString("avatar_url"));
        nv.setNgayTao(rs.getTimestamp("ngay_tao"));
        
        // Set tên phòng ban nếu có
        String tenPhongBan = rs.getString("ten_phong_ban");
        if (tenPhongBan != null) {
            nv.setTenPhongBan(tenPhongBan);
        }
        
        return nv;
    }
}
