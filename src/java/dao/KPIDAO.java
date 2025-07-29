package dao;

import model.KPI;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KPIDAO {
    
    // Lấy tất cả KPI
    public List<KPI> getAllKPI() {
        List<KPI> dsKPI = new ArrayList<>();
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "ORDER BY k.nam DESC, k.thang DESC, nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                KPI kpi = mapResultSetToKPI(rs);
                dsKPI.add(kpi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsKPI;
    }
    
    // Lấy KPI theo tháng năm
    public List<KPI> getKPIByMonth(int thang, int nam) {
        List<KPI> dsKPI = new ArrayList<>();
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE k.thang = ? AND k.nam = ? " +
                    "ORDER BY k.diem_kpi DESC, nv.ho_ten";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KPI kpi = mapResultSetToKPI(rs);
                    dsKPI.add(kpi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsKPI;
    }
    
    // Lấy KPI theo nhân viên
    public List<KPI> getKPIByNhanVien(int nhanVienId) {
        List<KPI> dsKPI = new ArrayList<>();
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE k.nhan_vien_id = ? " +
                    "ORDER BY k.nam DESC, k.thang DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KPI kpi = mapResultSetToKPI(rs);
                    dsKPI.add(kpi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsKPI;
    }
    
    // Lấy KPI theo ID
    public KPI getKPIById(int id) {
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE k.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKPI(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Thêm KPI mới
    public boolean addKPI(KPI kpi) {
        String sql = "INSERT INTO luu_kpi (nhan_vien_id, thang, nam, chi_tieu, ket_qua, diem_kpi, ghi_chu) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, kpi.getNhanVienId());
            stmt.setInt(2, kpi.getThang());
            stmt.setInt(3, kpi.getNam());
            stmt.setString(4, kpi.getChiTieu());
            stmt.setString(5, kpi.getKetQua());
            stmt.setFloat(6, kpi.getDiemKpi());
            stmt.setString(7, kpi.getGhiChu());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật KPI
    public boolean updateKPI(KPI kpi) {
        String sql = "UPDATE luu_kpi SET chi_tieu = ?, ket_qua = ?, diem_kpi = ?, ghi_chu = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kpi.getChiTieu());
            stmt.setString(2, kpi.getKetQua());
            stmt.setFloat(3, kpi.getDiemKpi());
            stmt.setString(4, kpi.getGhiChu());
            stmt.setInt(5, kpi.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa KPI
    public boolean deleteKPI(int id) {
        String sql = "DELETE FROM luu_kpi WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy điểm KPI trung bình của nhân viên
    public float getDiemKPITrungBinh(int nhanVienId, int nam) {
        String sql = "SELECT AVG(diem_kpi) as trung_binh FROM luu_kpi WHERE nhan_vien_id = ? AND nam = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nhanVienId);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("trung_binh");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0f;
    }
    
    // Lấy top nhân viên KPI cao nhất
    public List<KPI> getTopKPI(int thang, int nam, int limit) {
        List<KPI> dsKPI = new ArrayList<>();
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE k.thang = ? AND k.nam = ? " +
                    "ORDER BY k.diem_kpi DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            stmt.setInt(3, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KPI kpi = mapResultSetToKPI(rs);
                    dsKPI.add(kpi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsKPI;
    }
    
    // Kiểm tra đã có KPI tháng này chưa
    public boolean existsKPIThangNay(int nhanVienId, int thang, int nam) {
        String sql = "SELECT COUNT(*) as count FROM luu_kpi WHERE nhan_vien_id = ? AND thang = ? AND nam = ?";
        
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
    
    // Lấy KPI theo phòng ban và tháng
    public List<KPI> getKPIByPhongBanAndMonth(int phongBanId, int thang, int nam) {
        List<KPI> dsKPI = new ArrayList<>();
        String sql = "SELECT k.*, nv.ho_ten, pb.ten_phong " +
                    "FROM luu_kpi k " +
                    "JOIN nhanvien nv ON k.nhan_vien_id = nv.id " +
                    "LEFT JOIN phong_ban pb ON nv.phong_ban_id = pb.id " +
                    "WHERE nv.phong_ban_id = ? AND k.thang = ? AND k.nam = ? " +
                    "ORDER BY k.diem_kpi DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, phongBanId);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KPI kpi = mapResultSetToKPI(rs);
                    dsKPI.add(kpi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsKPI;
    }
    
    // Map ResultSet to KPI object
    private KPI mapResultSetToKPI(ResultSet rs) throws SQLException {
        KPI kpi = new KPI();
        kpi.setId(rs.getInt("id"));
        kpi.setNhanVienId(rs.getInt("nhan_vien_id"));
        kpi.setThang(rs.getInt("thang"));
        kpi.setNam(rs.getInt("nam"));
        kpi.setChiTieu(rs.getString("chi_tieu"));
        kpi.setKetQua(rs.getString("ket_qua"));
        kpi.setDiemKpi(rs.getFloat("diem_kpi"));
        kpi.setGhiChu(rs.getString("ghi_chu"));
        kpi.setNgayTao(rs.getTimestamp("ngay_tao"));
        kpi.setHoTenNhanVien(rs.getString("ho_ten"));
        kpi.setTenPhongBan(rs.getString("ten_phong"));
        
        return kpi;
    }
}
