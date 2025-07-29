package dao;

import model.ThongBao;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongBaoDAO {
    
    // Lấy tất cả thông báo
    public List<ThongBao> getAllThongBao() {
        List<ThongBao> dsThongBao = new ArrayList<>();
        String sql = "SELECT tb.*, nv.ho_ten " +
                    "FROM thong_bao tb " +
                    "JOIN nhanvien nv ON tb.nguoi_nhan_id = nv.id " +
                    "ORDER BY tb.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ThongBao thongBao = mapResultSetToThongBao(rs);
                dsThongBao.add(thongBao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsThongBao;
    }
    
    // Lấy thông báo theo người nhận
    public List<ThongBao> getThongBaoByNguoiNhan(int nguoiNhanId) {
        List<ThongBao> dsThongBao = new ArrayList<>();
        String sql = "SELECT tb.*, nv.ho_ten " +
                    "FROM thong_bao tb " +
                    "JOIN nhanvien nv ON tb.nguoi_nhan_id = nv.id " +
                    "WHERE tb.nguoi_nhan_id = ? " +
                    "ORDER BY tb.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiNhanId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongBao thongBao = mapResultSetToThongBao(rs);
                    dsThongBao.add(thongBao);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsThongBao;
    }
    
    // Lấy thông báo chưa đọc theo người nhận
    public List<ThongBao> getThongBaoChuaDoc(int nguoiNhanId) {
        List<ThongBao> dsThongBao = new ArrayList<>();
        String sql = "SELECT tb.*, nv.ho_ten " +
                    "FROM thong_bao tb " +
                    "JOIN nhanvien nv ON tb.nguoi_nhan_id = nv.id " +
                    "WHERE tb.nguoi_nhan_id = ? AND tb.da_doc = false " +
                    "ORDER BY tb.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiNhanId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongBao thongBao = mapResultSetToThongBao(rs);
                    dsThongBao.add(thongBao);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsThongBao;
    }
    
    // Lấy thông báo theo ID
    public ThongBao getThongBaoById(int id) {
        String sql = "SELECT tb.*, nv.ho_ten " +
                    "FROM thong_bao tb " +
                    "JOIN nhanvien nv ON tb.nguoi_nhan_id = nv.id " +
                    "WHERE tb.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToThongBao(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Thêm thông báo mới
    public boolean addThongBao(ThongBao thongBao) {
        String sql = "INSERT INTO thong_bao (tieu_de, noi_dung, nguoi_nhan_id, loai_thong_bao, da_doc) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, thongBao.getTieuDe());
            stmt.setString(2, thongBao.getNoiDung());
            stmt.setInt(3, thongBao.getNguoiNhanId());
            stmt.setString(4, thongBao.getLoaiThongBao());
            stmt.setBoolean(5, thongBao.isDaDoc());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Gửi thông báo cho nhiều người
    public boolean addThongBaoChoNhieuNguoi(String tieuDe, String noiDung, String loaiThongBao, List<Integer> dsNguoiNhanId) {
        String sql = "INSERT INTO thong_bao (tieu_de, noi_dung, nguoi_nhan_id, loai_thong_bao, da_doc) " +
                    "VALUES (?, ?, ?, ?, false)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (int nguoiNhanId : dsNguoiNhanId) {
                stmt.setString(1, tieuDe);
                stmt.setString(2, noiDung);
                stmt.setInt(3, nguoiNhanId);
                stmt.setString(4, loaiThongBao);
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            conn.commit();
            
            return results.length == dsNguoiNhanId.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Đánh dấu đã đọc
    public boolean danhDauDaDoc(int id) {
        String sql = "UPDATE thong_bao SET da_doc = true WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Đánh dấu tất cả đã đọc
    public boolean danhDauTatCaDaDoc(int nguoiNhanId) {
        String sql = "UPDATE thong_bao SET da_doc = true WHERE nguoi_nhan_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiNhanId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa thông báo
    public boolean deleteThongBao(int id) {
        String sql = "DELETE FROM thong_bao WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Đếm số thông báo chưa đọc
    public int countThongBaoChuaDoc(int nguoiNhanId) {
        String sql = "SELECT COUNT(*) as count FROM thong_bao WHERE nguoi_nhan_id = ? AND da_doc = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiNhanId);
            
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
    
    // Lấy thông báo mới nhất (top 5)
    public List<ThongBao> getThongBaoMoiNhat(int nguoiNhanId, int limit) {
        List<ThongBao> dsThongBao = new ArrayList<>();
        String sql = "SELECT tb.*, nv.ho_ten " +
                    "FROM thong_bao tb " +
                    "JOIN nhanvien nv ON tb.nguoi_nhan_id = nv.id " +
                    "WHERE tb.nguoi_nhan_id = ? " +
                    "ORDER BY tb.ngay_tao DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nguoiNhanId);
            stmt.setInt(2, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongBao thongBao = mapResultSetToThongBao(rs);
                    dsThongBao.add(thongBao);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsThongBao;
    }
    
    // Map ResultSet to ThongBao object
    private ThongBao mapResultSetToThongBao(ResultSet rs) throws SQLException {
        ThongBao thongBao = new ThongBao();
        thongBao.setId(rs.getInt("id"));
        thongBao.setTieuDe(rs.getString("tieu_de"));
        thongBao.setNoiDung(rs.getString("noi_dung"));
        thongBao.setNguoiNhanId(rs.getInt("nguoi_nhan_id"));
        thongBao.setLoaiThongBao(rs.getString("loai_thong_bao"));
        thongBao.setDaDoc(rs.getBoolean("da_doc"));
        thongBao.setNgayTao(rs.getTimestamp("ngay_tao"));
        thongBao.setHoTenNguoiNhan(rs.getString("ho_ten"));
        
        return thongBao;
    }
}
