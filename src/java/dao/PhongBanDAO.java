package dao;

import model.PhongBan;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAO {
    
    // Lấy danh sách tất cả phòng ban
    public List<PhongBan> getAllPhongBan() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT pb.*, nv.ho_ten as ten_truong_phong, " +
                    "(SELECT COUNT(*) FROM nhanvien WHERE phong_ban_id = pb.id AND trang_thai_lam_viec = 'DangLam') as so_nhan_vien " +
                    "FROM phong_ban pb " +
                    "LEFT JOIN nhanvien nv ON pb.truong_phong_id = nv.id " +
                    "ORDER BY pb.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setId(rs.getInt("id"));
                pb.setTenPhong(rs.getString("ten_phong"));
                pb.setTruongPhongId(rs.getInt("truong_phong_id"));
                pb.setNgayTao(rs.getTimestamp("ngay_tao"));
                pb.setTenTruongPhong(rs.getString("ten_truong_phong"));
                pb.setSoNhanVien(rs.getInt("so_nhan_vien"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy phòng ban theo ID
    public PhongBan getPhongBanById(int id) {
        String sql = "SELECT pb.*, nv.ho_ten as ten_truong_phong, " +
                    "(SELECT COUNT(*) FROM nhanvien WHERE phong_ban_id = pb.id AND trang_thai_lam_viec = 'DangLam') as so_nhan_vien " +
                    "FROM phong_ban pb " +
                    "LEFT JOIN nhanvien nv ON pb.truong_phong_id = nv.id " +
                    "WHERE pb.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setId(rs.getInt("id"));
                pb.setTenPhong(rs.getString("ten_phong"));
                pb.setTruongPhongId(rs.getInt("truong_phong_id"));
                pb.setNgayTao(rs.getTimestamp("ngay_tao"));
                pb.setTenTruongPhong(rs.getString("ten_truong_phong"));
                pb.setSoNhanVien(rs.getInt("so_nhan_vien"));
                return pb;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm phòng ban mới
    public boolean addPhongBan(PhongBan phongBan) {
        String sql = "INSERT INTO phong_ban (ten_phong, truong_phong_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phongBan.getTenPhong());
            if (phongBan.getTruongPhongId() > 0) {
                ps.setInt(2, phongBan.getTruongPhongId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin phòng ban
    public boolean updatePhongBan(PhongBan phongBan) {
        String sql = "UPDATE phong_ban SET ten_phong = ?, truong_phong_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phongBan.getTenPhong());
            if (phongBan.getTruongPhongId() > 0) {
                ps.setInt(2, phongBan.getTruongPhongId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, phongBan.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa phòng ban
    public boolean deletePhongBan(int id) {
        // Kiểm tra xem phòng ban có nhân viên không
        if (hasEmployees(id)) {
            return false; // Không thể xóa phòng ban có nhân viên
        }
        
        String sql = "DELETE FROM phong_ban WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra phòng ban có nhân viên không
    public boolean hasEmployees(int phongBanId) {
        String sql = "SELECT COUNT(*) FROM nhanvien WHERE phong_ban_id = ? AND trang_thai_lam_viec = 'DangLam'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, phongBanId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Tìm kiếm phòng ban
    public List<PhongBan> searchPhongBan(String keyword) {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT pb.*, nv.ho_ten as ten_truong_phong, " +
                    "(SELECT COUNT(*) FROM nhanvien WHERE phong_ban_id = pb.id AND trang_thai_lam_viec = 'DangLam') as so_nhan_vien " +
                    "FROM phong_ban pb " +
                    "LEFT JOIN nhanvien nv ON pb.truong_phong_id = nv.id " +
                    "WHERE pb.ten_phong LIKE ? OR nv.ho_ten LIKE ? " +
                    "ORDER BY pb.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setId(rs.getInt("id"));
                pb.setTenPhong(rs.getString("ten_phong"));
                pb.setTruongPhongId(rs.getInt("truong_phong_id"));
                pb.setNgayTao(rs.getTimestamp("ngay_tao"));
                pb.setTenTruongPhong(rs.getString("ten_truong_phong"));
                pb.setSoNhanVien(rs.getInt("so_nhan_vien"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra tên phòng ban đã tồn tại
    public boolean isTenPhongExists(String tenPhong, int excludeId) {
        String sql = "SELECT COUNT(*) FROM phong_ban WHERE ten_phong = ? AND id != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tenPhong);
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
    
    // Lấy danh sách phòng ban cho dropdown
    public List<PhongBan> getPhongBanForDropdown() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT id, ten_phong FROM phong_ban ORDER BY ten_phong";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setId(rs.getInt("id"));
                pb.setTenPhong(rs.getString("ten_phong"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
