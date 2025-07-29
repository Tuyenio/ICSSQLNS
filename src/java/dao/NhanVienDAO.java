package dao;

import model.NhanVien;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    
    // Đăng nhập
    public NhanVien login(String email, String matKhau) {
        String sql = "SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE nv.email = ? AND nv.mat_khau = ? AND nv.trang_thai_lam_viec = 'DangLam'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, matKhau);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToNhanVien(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy danh sách tất cả nhân viên với thông tin phòng ban
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
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nhanVien) {
        String sql = "INSERT INTO nhanvien (ho_ten, email, mat_khau, so_dien_thoai, gioi_tinh, " +
                    "ngay_sinh, phong_ban_id, chuc_vu, trang_thai_lam_viec, vai_tro, ngay_vao_lam, avatar_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nhanVien.getHoTen());
            ps.setString(2, nhanVien.getEmail());
            ps.setString(3, nhanVien.getMatKhau());
            ps.setString(4, nhanVien.getSoDienThoai());
            ps.setString(5, nhanVien.getGioiTinh());
            ps.setDate(6, nhanVien.getNgaySinh());
            ps.setInt(7, nhanVien.getPhongBanId());
            ps.setString(8, nhanVien.getChucVu());
            ps.setString(9, nhanVien.getTrangThaiLamViec());
            ps.setString(10, nhanVien.getVaiTro());
            ps.setDate(11, nhanVien.getNgayVaoLam());
            ps.setString(12, nhanVien.getAvatarUrl());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nhanVien) {
        String sql = "UPDATE nhanvien SET ho_ten = ?, email = ?, so_dien_thoai = ?, gioi_tinh = ?, " +
                    "ngay_sinh = ?, phong_ban_id = ?, chuc_vu = ?, trang_thai_lam_viec = ?, " +
                    "vai_tro = ?, ngay_vao_lam = ?, avatar_url = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nhanVien.getHoTen());
            ps.setString(2, nhanVien.getEmail());
            ps.setString(3, nhanVien.getSoDienThoai());
            ps.setString(4, nhanVien.getGioiTinh());
            ps.setDate(5, nhanVien.getNgaySinh());
            ps.setInt(6, nhanVien.getPhongBanId());
            ps.setString(7, nhanVien.getChucVu());
            ps.setString(8, nhanVien.getTrangThaiLamViec());
            ps.setString(9, nhanVien.getVaiTro());
            ps.setDate(10, nhanVien.getNgayVaoLam());
            ps.setString(11, nhanVien.getAvatarUrl());
            ps.setInt(12, nhanVien.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa nhân viên
    public boolean deleteNhanVien(int id) {
        String sql = "DELETE FROM nhanvien WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tìm kiếm nhân viên
    public List<NhanVien> searchNhanVien(String keyword, String phongBan, String trangThai) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                                            "FROM nhanvien nv " +
                                            "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (nv.ho_ten LIKE ? OR nv.email LIKE ? OR nv.so_dien_thoai LIKE ?)");
            String searchTerm = "%" + keyword.trim() + "%";
            params.add(searchTerm);
            params.add(searchTerm);
            params.add(searchTerm);
        }
        
        if (phongBan != null && !phongBan.trim().isEmpty() && !"all".equals(phongBan)) {
            sql.append(" AND nv.phong_ban_id = ?");
            params.add(Integer.parseInt(phongBan));
        }
        
        if (trangThai != null && !trangThai.trim().isEmpty() && !"all".equals(trangThai)) {
            sql.append(" AND nv.trang_thai_lam_viec = ?");
            params.add(trangThai);
        }
        
        sql.append(" ORDER BY nv.id DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
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
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy nhân viên theo phòng ban
    public List<NhanVien> getNhanVienByPhongBan(int phongBanId) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, pb.ten_phong as ten_phong_ban " +
                    "FROM nhanvien nv " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE nv.phong_ban_id = ? AND nv.trang_thai_lam_viec = 'DangLam' " +
                    "ORDER BY nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, phongBanId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra email đã tồn tại
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
        nv.setGioiTinh(rs.getString("gioi_tinh"));
        nv.setNgaySinh(rs.getDate("ngay_sinh"));
        nv.setPhongBanId(rs.getInt("phong_ban_id"));
        nv.setChucVu(rs.getString("chuc_vu"));
        nv.setTrangThaiLamViec(rs.getString("trang_thai_lam_viec"));
        nv.setVaiTro(rs.getString("vai_tro"));
        nv.setNgayVaoLam(rs.getDate("ngay_vao_lam"));
        nv.setAvatarUrl(rs.getString("avatar_url"));
        nv.setNgayTao(rs.getTimestamp("ngay_tao"));
        nv.setTenPhongBan(rs.getString("ten_phong_ban"));
        return nv;
    }
}
