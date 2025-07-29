package dao;

import model.NhomThanhVien;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhomThanhVienDAO {
    
    // Lấy danh sách thành viên theo nhóm
    public List<NhomThanhVien> getThanhVienByNhom(int nhomId) {
        List<NhomThanhVien> danhSach = new ArrayList<>();
        String sql = """
            SELECT ntv.*, ncv.ten_nhom, nv.ho_ten as ten_nhan_vien, nv.email as email_nhan_vien, nv.chuc_vu
            FROM nhom_thanh_vien ntv
            LEFT JOIN nhom_cong_viec ncv ON ntv.nhom_id = ncv.id
            LEFT JOIN nhanvien nv ON ntv.nhan_vien_id = nv.id
            WHERE ntv.nhom_id = ?
            ORDER BY ntv.vai_tro_nhom DESC, nv.ho_ten ASC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NhomThanhVien thanhVien = new NhomThanhVien();
                thanhVien.setId(rs.getInt("id"));
                thanhVien.setNhomId(rs.getInt("nhom_id"));
                thanhVien.setNhanVienId(rs.getInt("nhan_vien_id"));
                thanhVien.setVaiTroNhom(rs.getString("vai_tro_nhom"));
                thanhVien.setTenNhom(rs.getString("ten_nhom"));
                thanhVien.setTenNhanVien(rs.getString("ten_nhan_vien"));
                thanhVien.setEmailNhanVien(rs.getString("email_nhan_vien"));
                thanhVien.setChucVu(rs.getString("chuc_vu"));
                danhSach.add(thanhVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Lấy danh sách nhóm theo nhân viên
    public List<NhomThanhVien> getNhomByNhanVien(int nhanVienId) {
        List<NhomThanhVien> danhSach = new ArrayList<>();
        String sql = """
            SELECT ntv.*, ncv.ten_nhom, nv.ho_ten as ten_nhan_vien, nv.email as email_nhan_vien, nv.chuc_vu
            FROM nhom_thanh_vien ntv
            LEFT JOIN nhom_cong_viec ncv ON ntv.nhom_id = ncv.id
            LEFT JOIN nhanvien nv ON ntv.nhan_vien_id = nv.id
            WHERE ntv.nhan_vien_id = ?
            ORDER BY ncv.ngay_tao DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NhomThanhVien thanhVien = new NhomThanhVien();
                thanhVien.setId(rs.getInt("id"));
                thanhVien.setNhomId(rs.getInt("nhom_id"));
                thanhVien.setNhanVienId(rs.getInt("nhan_vien_id"));
                thanhVien.setVaiTroNhom(rs.getString("vai_tro_nhom"));
                thanhVien.setTenNhom(rs.getString("ten_nhom"));
                thanhVien.setTenNhanVien(rs.getString("ten_nhan_vien"));
                thanhVien.setEmailNhanVien(rs.getString("email_nhan_vien"));
                thanhVien.setChucVu(rs.getString("chuc_vu"));
                danhSach.add(thanhVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Thêm thành viên vào nhóm
    public boolean addThanhVienToNhom(NhomThanhVien thanhVien) {
        String sql = "INSERT INTO nhom_thanh_vien (nhom_id, nhan_vien_id, vai_tro_nhom) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thanhVien.getNhomId());
            stmt.setInt(2, thanhVien.getNhanVienId());
            stmt.setString(3, thanhVien.getVaiTroNhom());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa thành viên khỏi nhóm
    public boolean removeThanhVienFromNhom(int nhomId, int nhanVienId) {
        String sql = "DELETE FROM nhom_thanh_vien WHERE nhom_id = ? AND nhan_vien_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            stmt.setInt(2, nhanVienId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật vai trò thành viên trong nhóm
    public boolean updateVaiTroThanhVien(int nhomId, int nhanVienId, String vaiTroMoi) {
        String sql = "UPDATE nhom_thanh_vien SET vai_tro_nhom = ? WHERE nhom_id = ? AND nhan_vien_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vaiTroMoi);
            stmt.setInt(2, nhomId);
            stmt.setInt(3, nhanVienId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra nhân viên có trong nhóm không
    public boolean isNhanVienInNhom(int nhomId, int nhanVienId) {
        String sql = "SELECT COUNT(*) FROM nhom_thanh_vien WHERE nhom_id = ? AND nhan_vien_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            stmt.setInt(2, nhanVienId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy vai trò của nhân viên trong nhóm
    public String getVaiTroNhanVienInNhom(int nhomId, int nhanVienId) {
        String sql = "SELECT vai_tro_nhom FROM nhom_thanh_vien WHERE nhom_id = ? AND nhan_vien_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            stmt.setInt(2, nhanVienId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("vai_tro_nhom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy danh sách nhóm trưởng của nhóm
    public List<NhomThanhVien> getNhomTruongByNhom(int nhomId) {
        List<NhomThanhVien> danhSach = new ArrayList<>();
        String sql = """
            SELECT ntv.*, ncv.ten_nhom, nv.ho_ten as ten_nhan_vien, nv.email as email_nhan_vien, nv.chuc_vu
            FROM nhom_thanh_vien ntv
            LEFT JOIN nhom_cong_viec ncv ON ntv.nhom_id = ncv.id
            LEFT JOIN nhanvien nv ON ntv.nhan_vien_id = nv.id
            WHERE ntv.nhom_id = ? AND ntv.vai_tro_nhom = 'NhomTruong'
            ORDER BY nv.ho_ten ASC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NhomThanhVien thanhVien = new NhomThanhVien();
                thanhVien.setId(rs.getInt("id"));
                thanhVien.setNhomId(rs.getInt("nhom_id"));
                thanhVien.setNhanVienId(rs.getInt("nhan_vien_id"));
                thanhVien.setVaiTroNhom(rs.getString("vai_tro_nhom"));
                thanhVien.setTenNhom(rs.getString("ten_nhom"));
                thanhVien.setTenNhanVien(rs.getString("ten_nhan_vien"));
                thanhVien.setEmailNhanVien(rs.getString("email_nhan_vien"));
                thanhVien.setChucVu(rs.getString("chuc_vu"));
                danhSach.add(thanhVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Xóa tất cả thành viên của nhóm (dùng khi xóa nhóm)
    public boolean deleteAllThanhVienByNhom(int nhomId) {
        String sql = "DELETE FROM nhom_thanh_vien WHERE nhom_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhomId);
            return stmt.executeUpdate() >= 0; // Trả về true ngay cả khi không có thành viên nào
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
