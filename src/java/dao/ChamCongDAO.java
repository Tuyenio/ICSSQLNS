package dao;

import model.ChamCong;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamCongDAO {
    
    // Lấy tất cả chấm công
    public List<ChamCong> getAllChamCong() {
        List<ChamCong> dsChamCong = new ArrayList<>();
        String sql = "SELECT cc.*, nv.ho_ten, pb.ten_phong " +
                    "FROM cham_cong cc " +
                    "JOIN nhanvien nv ON cc.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "ORDER BY cc.ngay DESC, nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ChamCong chamCong = new ChamCong();
                chamCong.setId(rs.getInt("id"));
                chamCong.setNhanVienId(rs.getInt("nhan_vien_id"));
                chamCong.setNgay(rs.getDate("ngay"));
                chamCong.setCheckIn(rs.getTime("check_in"));
                chamCong.setCheckOut(rs.getTime("check_out"));
                chamCong.setHoTenNhanVien(rs.getString("ho_ten"));
                chamCong.setTenPhongBan(rs.getString("ten_phong"));
                
                dsChamCong.add(chamCong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsChamCong;
    }
    
    // Lấy chấm công theo nhân viên và tháng
    public List<ChamCong> getChamCongByNhanVienAndMonth(int nhanVienId, int thang, int nam) {
        List<ChamCong> dsChamCong = new ArrayList<>();
        String sql = "SELECT cc.*, nv.ho_ten, pb.ten_phong " +
                    "FROM cham_cong cc " +
                    "JOIN nhanvien nv ON cc.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE cc.nhan_vien_id = ? AND MONTH(cc.ngay) = ? AND YEAR(cc.ngay) = ? " +
                    "ORDER BY cc.ngay";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChamCong chamCong = new ChamCong();
                    chamCong.setId(rs.getInt("id"));
                    chamCong.setNhanVienId(rs.getInt("nhan_vien_id"));
                    chamCong.setNgay(rs.getDate("ngay"));
                    chamCong.setCheckIn(rs.getTime("check_in"));
                    chamCong.setCheckOut(rs.getTime("check_out"));
                    chamCong.setHoTenNhanVien(rs.getString("ho_ten"));
                    chamCong.setTenPhongBan(rs.getString("ten_phong"));
                    
                    dsChamCong.add(chamCong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsChamCong;
    }
    
    // Lấy chấm công theo tháng năm
    public List<ChamCong> getChamCongByMonth(int thang, int nam) {
        List<ChamCong> dsChamCong = new ArrayList<>();
        String sql = "SELECT cc.*, nv.ho_ten, pb.ten_phong " +
                    "FROM cham_cong cc " +
                    "JOIN nhanvien nv ON cc.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE MONTH(cc.ngay) = ? AND YEAR(cc.ngay) = ? " +
                    "ORDER BY cc.ngay DESC, nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChamCong chamCong = new ChamCong();
                    chamCong.setId(rs.getInt("id"));
                    chamCong.setNhanVienId(rs.getInt("nhan_vien_id"));
                    chamCong.setNgay(rs.getDate("ngay"));
                    chamCong.setCheckIn(rs.getTime("check_in"));
                    chamCong.setCheckOut(rs.getTime("check_out"));
                    chamCong.setHoTenNhanVien(rs.getString("ho_ten"));
                    chamCong.setTenPhongBan(rs.getString("ten_phong"));
                    
                    dsChamCong.add(chamCong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsChamCong;
    }
    
    // Check-in
    public boolean checkIn(int nhanVienId, Date ngay) {
        String sql = "INSERT INTO cham_cong (nhan_vien_id, ngay, check_in) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE check_in = VALUES(check_in)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            stmt.setTime(3, new Time(System.currentTimeMillis()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check-out
    public boolean checkOut(int nhanVienId, Date ngay) {
        String sql = "UPDATE cham_cong SET check_out = ? WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTime(1, new Time(System.currentTimeMillis()));
            stmt.setInt(2, nhanVienId);
            stmt.setDate(3, ngay);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra đã check-in chưa
    public boolean isCheckedIn(int nhanVienId, Date ngay) {
        String sql = "SELECT check_in FROM cham_cong WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getTime("check_in") != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra đã check-out chưa
    public boolean isCheckedOut(int nhanVienId, Date ngay) {
        String sql = "SELECT check_out FROM cham_cong WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getTime("check_out") != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy chấm công hôm nay của nhân viên
    public ChamCong getChamCongToday(int nhanVienId) {
        String sql = "SELECT cc.*, nv.ho_ten, pb.ten_phong " +
                    "FROM cham_cong cc " +
                    "JOIN nhanvien nv ON cc.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE cc.nhan_vien_id = ? AND cc.ngay = CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ChamCong chamCong = new ChamCong();
                    chamCong.setId(rs.getInt("id"));
                    chamCong.setNhanVienId(rs.getInt("nhan_vien_id"));
                    chamCong.setNgay(rs.getDate("ngay"));
                    chamCong.setCheckIn(rs.getTime("check_in"));
                    chamCong.setCheckOut(rs.getTime("check_out"));
                    chamCong.setHoTenNhanVien(rs.getString("ho_ten"));
                    chamCong.setTenPhongBan(rs.getString("ten_phong"));
                    
                    return chamCong;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Đếm số ngày làm việc trong tháng
    public int countWorkingDays(int nhanVienId, int thang, int nam) {
        String sql = "SELECT COUNT(*) as count FROM cham_cong " +
                    "WHERE nhan_vien_id = ? AND MONTH(ngay) = ? AND YEAR(ngay) = ? AND check_in IS NOT NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}
