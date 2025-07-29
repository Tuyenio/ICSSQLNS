package dao;

import model.NhomCongViec;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhomCongViecDAO {
    
    // Lấy danh sách tất cả nhóm công việc với thông tin join
    public List<NhomCongViec> getAllNhomCongViec() {
        List<NhomCongViec> danhSach = new ArrayList<>();
        String sql = """
            SELECT n.*, nv.ho_ten as ten_nguoi_tao,
                   (SELECT COUNT(*) FROM nhom_thanh_vien ntv WHERE ntv.nhom_id = n.id) as so_thanh_vien,
                   (SELECT COUNT(*) FROM cong_viec cv WHERE cv.nhom_id = n.id) as so_cong_viec
            FROM nhom_cong_viec n
            LEFT JOIN nhanvien nv ON n.nguoi_tao_id = nv.id
            ORDER BY n.ngay_tao DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                NhomCongViec nhom = new NhomCongViec();
                nhom.setId(rs.getInt("id"));
                nhom.setTenNhom(rs.getString("ten_nhom"));
                nhom.setMoTa(rs.getString("mo_ta"));
                nhom.setNguoiTaoId(rs.getInt("nguoi_tao_id"));
                nhom.setNgayTao(rs.getTimestamp("ngay_tao"));
                nhom.setTenNguoiTao(rs.getString("ten_nguoi_tao"));
                nhom.setSoThanhVien(rs.getInt("so_thanh_vien"));
                nhom.setSoCongViec(rs.getInt("so_cong_viec"));
                danhSach.add(nhom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Lấy nhóm công việc theo ID
    public NhomCongViec getNhomCongViecById(int id) {
        String sql = """
            SELECT n.*, nv.ho_ten as ten_nguoi_tao,
                   (SELECT COUNT(*) FROM nhom_thanh_vien ntv WHERE ntv.nhom_id = n.id) as so_thanh_vien,
                   (SELECT COUNT(*) FROM cong_viec cv WHERE cv.nhom_id = n.id) as so_cong_viec
            FROM nhom_cong_viec n
            LEFT JOIN nhanvien nv ON n.nguoi_tao_id = nv.id
            WHERE n.id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                NhomCongViec nhom = new NhomCongViec();
                nhom.setId(rs.getInt("id"));
                nhom.setTenNhom(rs.getString("ten_nhom"));
                nhom.setMoTa(rs.getString("mo_ta"));
                nhom.setNguoiTaoId(rs.getInt("nguoi_tao_id"));
                nhom.setNgayTao(rs.getTimestamp("ngay_tao"));
                nhom.setTenNguoiTao(rs.getString("ten_nguoi_tao"));
                nhom.setSoThanhVien(rs.getInt("so_thanh_vien"));
                nhom.setSoCongViec(rs.getInt("so_cong_viec"));
                return nhom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm nhóm công việc mới
    public boolean addNhomCongViec(NhomCongViec nhom) {
        String sql = "INSERT INTO nhom_cong_viec (ten_nhom, mo_ta, nguoi_tao_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nhom.getTenNhom());
            stmt.setString(2, nhom.getMoTa());
            stmt.setInt(3, nhom.getNguoiTaoId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    nhom.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật nhóm công việc
    public boolean updateNhomCongViec(NhomCongViec nhom) {
        String sql = "UPDATE nhom_cong_viec SET ten_nhom = ?, mo_ta = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nhom.getTenNhom());
            stmt.setString(2, nhom.getMoTa());
            stmt.setInt(3, nhom.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa nhóm công việc
    public boolean deleteNhomCongViec(int id) {
        String sql = "DELETE FROM nhom_cong_viec WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy nhóm công việc theo người tạo
    public List<NhomCongViec> getNhomCongViecByNguoiTao(int nguoiTaoId) {
        List<NhomCongViec> danhSach = new ArrayList<>();
        String sql = """
            SELECT n.*, nv.ho_ten as ten_nguoi_tao,
                   (SELECT COUNT(*) FROM nhom_thanh_vien ntv WHERE ntv.nhom_id = n.id) as so_thanh_vien,
                   (SELECT COUNT(*) FROM cong_viec cv WHERE cv.nhom_id = n.id) as so_cong_viec
            FROM nhom_cong_viec n
            LEFT JOIN nhanvien nv ON n.nguoi_tao_id = nv.id
            WHERE n.nguoi_tao_id = ?
            ORDER BY n.ngay_tao DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiTaoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NhomCongViec nhom = new NhomCongViec();
                nhom.setId(rs.getInt("id"));
                nhom.setTenNhom(rs.getString("ten_nhom"));
                nhom.setMoTa(rs.getString("mo_ta"));
                nhom.setNguoiTaoId(rs.getInt("nguoi_tao_id"));
                nhom.setNgayTao(rs.getTimestamp("ngay_tao"));
                nhom.setTenNguoiTao(rs.getString("ten_nguoi_tao"));
                nhom.setSoThanhVien(rs.getInt("so_thanh_vien"));
                nhom.setSoCongViec(rs.getInt("so_cong_viec"));
                danhSach.add(nhom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Lấy nhóm công việc theo thành viên
    public List<NhomCongViec> getNhomCongViecByThanhVien(int nhanVienId) {
        List<NhomCongViec> danhSach = new ArrayList<>();
        String sql = """
            SELECT DISTINCT n.*, nv.ho_ten as ten_nguoi_tao,
                   (SELECT COUNT(*) FROM nhom_thanh_vien ntv WHERE ntv.nhom_id = n.id) as so_thanh_vien,
                   (SELECT COUNT(*) FROM cong_viec cv WHERE cv.nhom_id = n.id) as so_cong_viec
            FROM nhom_cong_viec n
            LEFT JOIN nhanvien nv ON n.nguoi_tao_id = nv.id
            INNER JOIN nhom_thanh_vien ntv ON n.id = ntv.nhom_id
            WHERE ntv.nhan_vien_id = ?
            ORDER BY n.ngay_tao DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NhomCongViec nhom = new NhomCongViec();
                nhom.setId(rs.getInt("id"));
                nhom.setTenNhom(rs.getString("ten_nhom"));
                nhom.setMoTa(rs.getString("mo_ta"));
                nhom.setNguoiTaoId(rs.getInt("nguoi_tao_id"));
                nhom.setNgayTao(rs.getTimestamp("ngay_tao"));
                nhom.setTenNguoiTao(rs.getString("ten_nguoi_tao"));
                nhom.setSoThanhVien(rs.getInt("so_thanh_vien"));
                nhom.setSoCongViec(rs.getInt("so_cong_viec"));
                danhSach.add(nhom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Kiểm tra tên nhóm đã tồn tại
    public boolean isTenNhomExists(String tenNhom, int excludeId) {
        String sql = "SELECT COUNT(*) FROM nhom_cong_viec WHERE ten_nhom = ? AND id != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tenNhom);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
