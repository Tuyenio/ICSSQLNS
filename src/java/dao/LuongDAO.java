package dao;

import model.Luong;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LuongDAO {
    
    // Lấy tất cả lương
    public List<Luong> getAllLuong() {
        List<Luong> dsLuong = new ArrayList<>();
        String sql = "SELECT l.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luong l " +
                    "JOIN nhanvien nv ON l.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "ORDER BY l.nam DESC, l.thang DESC, nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Luong luong = mapResultSetToLuong(rs);
                dsLuong.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsLuong;
    }
    
    // Lấy lương theo tháng năm
    public List<Luong> getLuongByMonth(int thang, int nam) {
        List<Luong> dsLuong = new ArrayList<>();
        String sql = "SELECT l.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luong l " +
                    "JOIN nhanvien nv ON l.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE l.thang = ? AND l.nam = ? " +
                    "ORDER BY nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Luong luong = mapResultSetToLuong(rs);
                    dsLuong.add(luong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsLuong;
    }
    
    // Lấy lương theo nhân viên
    public List<Luong> getLuongByNhanVien(int nhanVienId) {
        List<Luong> dsLuong = new ArrayList<>();
        String sql = "SELECT l.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luong l " +
                    "JOIN nhanvien nv ON l.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE l.nhan_vien_id = ? " +
                    "ORDER BY l.nam DESC, l.thang DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Luong luong = mapResultSetToLuong(rs);
                    dsLuong.add(luong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsLuong;
    }
    
    // Lấy lương theo ID
    public Luong getLuongById(int id) {
        String sql = "SELECT l.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luong l " +
                    "JOIN nhanvien nv ON l.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE l.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLuong(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Thêm lương mới
    public boolean addLuong(Luong luong) {
        String sql = "INSERT INTO luong (nhan_vien_id, thang, nam, luong_co_ban, phu_cap, thuong, phat, " +
                    "bao_hiem, thue, luong_thuc_te, ghi_chu, trang_thai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Tính lương thực tế trước khi lưu
            luong.capNhatLuongThucTe();
            
            stmt.setInt(1, luong.getNhanVienId());
            stmt.setInt(2, luong.getThang());
            stmt.setInt(3, luong.getNam());
            stmt.setBigDecimal(4, luong.getLuongCoBan());
            stmt.setBigDecimal(5, luong.getPhuCap());
            stmt.setBigDecimal(6, luong.getThuong());
            stmt.setBigDecimal(7, luong.getPhat());
            stmt.setBigDecimal(8, luong.getBaoHiem());
            stmt.setBigDecimal(9, luong.getThue());
            stmt.setBigDecimal(10, luong.getLuongThucTe());
            stmt.setString(11, luong.getGhiChu());
            stmt.setString(12, luong.getTrangThai());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật lương
    public boolean updateLuong(Luong luong) {
        String sql = "UPDATE luong SET luong_co_ban = ?, phu_cap = ?, thuong = ?, phat = ?, " +
                    "bao_hiem = ?, thue = ?, luong_thuc_te = ?, ghi_chu = ?, trang_thai = ?, " +
                    "ngay_tra_luong = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Tính lương thực tế trước khi cập nhật
            luong.capNhatLuongThucTe();
            
            stmt.setBigDecimal(1, luong.getLuongCoBan());
            stmt.setBigDecimal(2, luong.getPhuCap());
            stmt.setBigDecimal(3, luong.getThuong());
            stmt.setBigDecimal(4, luong.getPhat());
            stmt.setBigDecimal(5, luong.getBaoHiem());
            stmt.setBigDecimal(6, luong.getThue());
            stmt.setBigDecimal(7, luong.getLuongThucTe());
            stmt.setString(8, luong.getGhiChu());
            stmt.setString(9, luong.getTrangThai());
            stmt.setDate(10, luong.getNgayTraLuong());
            stmt.setInt(11, luong.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa lương
    public boolean deleteLuong(int id) {
        String sql = "DELETE FROM luong WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Trả lương
    public boolean traLuong(int id) {
        String sql = "UPDATE luong SET trang_thai = 'DaTra', ngay_tra_luong = CURDATE() WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra đã có lương tháng này chưa
    public boolean existsLuongThangNay(int nhanVienId, int thang, int nam) {
        String sql = "SELECT COUNT(*) as count FROM luong WHERE nhan_vien_id = ? AND thang = ? AND nam = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Tính tổng lương tháng
    public BigDecimal getTongLuongThang(int thang, int nam) {
        String sql = "SELECT SUM(luong_thuc_te) as tong FROM luong WHERE thang = ? AND nam = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("tong");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    // Map ResultSet to Luong object
    private Luong mapResultSetToLuong(ResultSet rs) throws SQLException {
        Luong luong = new Luong();
        luong.setId(rs.getInt("id"));
        luong.setNhanVienId(rs.getInt("nhan_vien_id"));
        luong.setThang(rs.getInt("thang"));
        luong.setNam(rs.getInt("nam"));
        luong.setLuongCoBan(rs.getBigDecimal("luong_co_ban"));
        luong.setPhuCap(rs.getBigDecimal("phu_cap"));
        luong.setThuong(rs.getBigDecimal("thuong"));
        luong.setPhat(rs.getBigDecimal("phat"));
        luong.setBaoHiem(rs.getBigDecimal("bao_hiem"));
        luong.setThue(rs.getBigDecimal("thue"));
        luong.setLuongThucTe(rs.getBigDecimal("luong_thuc_te"));
        luong.setGhiChu(rs.getString("ghi_chu"));
        luong.setTrangThai(rs.getString("trang_thai"));
        luong.setNgayTraLuong(rs.getDate("ngay_tra_luong"));
        luong.setNgayTao(rs.getTimestamp("ngay_tao"));
        luong.setHoTenNhanVien(rs.getString("ho_ten"));
        luong.setTenPhongBan(rs.getString("ten_phong"));
        
        return luong;
    }
    
    // Calculate salary for an employee
    public BigDecimal calculateSalary(int nhanVienId, int thang, int nam) {
        String sql = "SELECT luong_co_ban FROM nhanvien WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nhanVienId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("luong_co_ban");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    // Get salary by month and year (alias for getLuongByMonth)
    public List<Luong> getLuongByThangNam(int thang, int nam) {
        return getLuongByMonth(thang, nam);
    }
    
    // Approve salary
    public boolean approveLuong(int id) {
        String sql = "UPDATE luong SET trang_thai = 'Đã duyệt' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Reject salary
    public boolean rejectLuong(int id, String lyDo) {
        String sql = "UPDATE luong SET trang_thai = 'Từ chối', ghi_chu = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lyDo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get total salary by month
    public BigDecimal getTongLuongTheoThang(java.util.Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int thang = cal.get(java.util.Calendar.MONTH) + 1;
        int nam = cal.get(java.util.Calendar.YEAR);
        return getTongLuongThang(thang, nam);
    }
    
    // Get average salary
    public BigDecimal getLuongTrungBinh() {
        String sql = "SELECT AVG(luong_thuc_te) as luong_tb FROM luong WHERE trang_thai = 'Đã trả'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("luong_tb");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    // Get number of employees paid
    public int getSoNhanVienDuocTraLuong(java.util.Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int thang = cal.get(java.util.Calendar.MONTH) + 1;
        int nam = cal.get(java.util.Calendar.YEAR);
        
        String sql = "SELECT COUNT(DISTINCT nhan_vien_id) as so_nv FROM luong WHERE thang = ? AND nam = ? AND trang_thai = 'Đã trả'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("so_nv");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
