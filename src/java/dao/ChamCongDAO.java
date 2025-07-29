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
                dsChamCong.add(mapResultSetToChamCong(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get all attendance error: " + e.getMessage());
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
                    "ORDER BY cc.ngay DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsChamCong.add(mapResultSetToChamCong(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Get attendance by employee and month error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dsChamCong;
    }
    
    // Thêm hoặc cập nhật check-in
    public boolean checkIn(int nhanVienId, Date ngay, Time gioVao) {
        // Kiểm tra đã check-in chưa
        if (hasCheckedIn(nhanVienId, ngay)) {
            return false; // Đã check-in rồi
        }
        
        String sql = "INSERT INTO cham_cong (nhan_vien_id, ngay, check_in) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE check_in = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            stmt.setTime(3, gioVao);
            stmt.setTime(4, gioVao);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Check-in error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật check-out
    public boolean checkOut(int nhanVienId, Date ngay, Time gioRa) {
        String sql = "UPDATE cham_cong SET check_out = ? WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTime(1, gioRa);
            stmt.setInt(2, nhanVienId);
            stmt.setDate(3, ngay);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Check-out error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra đã check-in chưa
    public boolean hasCheckedIn(int nhanVienId, Date ngay) {
        String sql = "SELECT check_in FROM cham_cong WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getTime("check_in") != null;
            }
        } catch (SQLException e) {
            System.err.println("Check has checked in error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra đã check-out chưa
    public boolean hasCheckedOut(int nhanVienId, Date ngay) {
        String sql = "SELECT check_out FROM cham_cong WHERE nhan_vien_id = ? AND ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getTime("check_out") != null;
            }
        } catch (SQLException e) {
            System.err.println("Check has checked out error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy bản ghi chấm công hôm nay
    public ChamCong getChamCongToday(int nhanVienId, Date ngay) {
        String sql = "SELECT cc.*, nv.ho_ten, pb.ten_phong " +
                    "FROM cham_cong cc " +
                    "JOIN nhanvien nv ON cc.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE cc.nhan_vien_id = ? AND cc.ngay = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChamCong(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Get attendance today error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Thống kê chấm công theo tháng
    public int countChamCongByMonth(int thang, int nam) {
        String sql = "SELECT COUNT(DISTINCT nhan_vien_id) FROM cham_cong " +
                    "WHERE MONTH(ngay) = ? AND YEAR(ngay) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Count attendance by month error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Map ResultSet to ChamCong object
    private ChamCong mapResultSetToChamCong(ResultSet rs) throws SQLException {
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
