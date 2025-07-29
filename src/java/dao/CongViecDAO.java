package dao;

import model.CongViec;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CongViecDAO {
    
    // Lấy danh sách tất cả công việc
    public List<CongViec> getAllCongViec() {
        List<CongViec> list = new ArrayList<>();
        String sql = "SELECT cv.*, " +
                    "ng.ho_ten as ten_nguoi_giao, " +
                    "nn.ho_ten as ten_nguoi_nhan, " +
                    "ncv.ten_nhom as ten_nhom, " +
                    "COALESCE(MAX(cvtd.phan_tram), 0) as phan_tram_tien_do " +
                    "FROM cong_viec cv " +
                    "LEFT JOIN nhanvien ng ON cv.nguoi_giao_id = ng.id " +
                    "LEFT JOIN nhanvien nn ON cv.nguoi_nhan_id = nn.id " +
                    "LEFT JOIN nhom_cong_viec ncv ON cv.nhom_id = ncv.id " +
                    "LEFT JOIN cong_viec_tien_do cvtd ON cv.id = cvtd.cong_viec_id " +
                    "GROUP BY cv.id " +
                    "ORDER BY cv.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToCongViec(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy công việc theo ID
    public CongViec getCongViecById(int id) {
        String sql = "SELECT cv.*, " +
                    "ng.ho_ten as ten_nguoi_giao, " +
                    "nn.ho_ten as ten_nguoi_nhan, " +
                    "ncv.ten_nhom as ten_nhom, " +
                    "COALESCE(MAX(cvtd.phan_tram), 0) as phan_tram_tien_do " +
                    "FROM cong_viec cv " +
                    "LEFT JOIN nhanvien ng ON cv.nguoi_giao_id = ng.id " +
                    "LEFT JOIN nhanvien nn ON cv.nguoi_nhan_id = nn.id " +
                    "LEFT JOIN nhom_cong_viec ncv ON cv.nhom_id = ncv.id " +
                    "LEFT JOIN cong_viec_tien_do cvtd ON cv.id = cvtd.cong_viec_id " +
                    "WHERE cv.id = ? " +
                    "GROUP BY cv.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCongViec(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm công việc mới
    public boolean addCongViec(CongViec congViec) {
        String sql = "INSERT INTO cong_viec (ten_cong_viec, mo_ta, han_hoan_thanh, muc_do_uu_tien, " +
                    "nguoi_giao_id, nguoi_nhan_id, nhom_id, trang_thai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, congViec.getTenCongViec());
            ps.setString(2, congViec.getMoTa());
            ps.setDate(3, congViec.getHanHoanThanh());
            ps.setString(4, congViec.getMucDoUuTien());
            ps.setInt(5, congViec.getNguoiGiaoId());
            ps.setInt(6, congViec.getNguoiNhanId());
            ps.setInt(7, congViec.getNhomId());
            ps.setString(8, congViec.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật công việc
    public boolean updateCongViec(CongViec congViec) {
        String sql = "UPDATE cong_viec SET ten_cong_viec = ?, mo_ta = ?, han_hoan_thanh = ?, " +
                    "muc_do_uu_tien = ?, nguoi_giao_id = ?, nguoi_nhan_id = ?, nhom_id = ?, trang_thai = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, congViec.getTenCongViec());
            ps.setString(2, congViec.getMoTa());
            ps.setDate(3, congViec.getHanHoanThanh());
            ps.setString(4, congViec.getMucDoUuTien());
            ps.setInt(5, congViec.getNguoiGiaoId());
            ps.setInt(6, congViec.getNguoiNhanId());
            ps.setInt(7, congViec.getNhomId());
            ps.setString(8, congViec.getTrangThai());
            ps.setInt(9, congViec.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa công việc
    public boolean deleteCongViec(int id) {
        String sql = "DELETE FROM cong_viec WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy công việc theo nhân viên
    public List<CongViec> getCongViecByNhanVien(int nhanVienId) {
        List<CongViec> list = new ArrayList<>();
        String sql = "SELECT cv.*, " +
                    "ng.ho_ten as ten_nguoi_giao, " +
                    "nn.ho_ten as ten_nguoi_nhan, " +
                    "ncv.ten_nhom as ten_nhom, " +
                    "COALESCE(MAX(cvtd.phan_tram), 0) as phan_tram_tien_do " +
                    "FROM cong_viec cv " +
                    "LEFT JOIN nhanvien ng ON cv.nguoi_giao_id = ng.id " +
                    "LEFT JOIN nhanvien nn ON cv.nguoi_nhan_id = nn.id " +
                    "LEFT JOIN nhom_cong_viec ncv ON cv.nhom_id = ncv.id " +
                    "LEFT JOIN cong_viec_tien_do cvtd ON cv.id = cvtd.cong_viec_id " +
                    "WHERE cv.nguoi_nhan_id = ? " +
                    "GROUP BY cv.id " +
                    "ORDER BY cv.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nhanVienId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToCongViec(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy công việc theo trạng thái
    public List<CongViec> getCongViecByTrangThai(String trangThai) {
        List<CongViec> list = new ArrayList<>();
        String sql = "SELECT cv.*, " +
                    "ng.ho_ten as ten_nguoi_giao, " +
                    "nn.ho_ten as ten_nguoi_nhan, " +
                    "ncv.ten_nhom as ten_nhom, " +
                    "COALESCE(MAX(cvtd.phan_tram), 0) as phan_tram_tien_do " +
                    "FROM cong_viec cv " +
                    "LEFT JOIN nhanvien ng ON cv.nguoi_giao_id = ng.id " +
                    "LEFT JOIN nhanvien nn ON cv.nguoi_nhan_id = nn.id " +
                    "LEFT JOIN nhom_cong_viec ncv ON cv.nhom_id = ncv.id " +
                    "LEFT JOIN cong_viec_tien_do cvtd ON cv.id = cvtd.cong_viec_id " +
                    "WHERE cv.trang_thai = ? " +
                    "GROUP BY cv.id " +
                    "ORDER BY cv.ngay_tao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToCongViec(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Cập nhật trạng thái công việc
    public boolean updateTrangThai(int id, String trangThai) {
        String sql = "UPDATE cong_viec SET trang_thai = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, trangThai);
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tìm kiếm công việc
    public List<CongViec> searchCongViec(String keyword, String trangThai, String mucDoUuTien) {
        List<CongViec> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT cv.*, " +
                                            "ng.ho_ten as ten_nguoi_giao, " +
                                            "nn.ho_ten as ten_nguoi_nhan, " +
                                            "ncv.ten_nhom as ten_nhom, " +
                                            "COALESCE(MAX(cvtd.phan_tram), 0) as phan_tram_tien_do " +
                                            "FROM cong_viec cv " +
                                            "LEFT JOIN nhanvien ng ON cv.nguoi_giao_id = ng.id " +
                                            "LEFT JOIN nhanvien nn ON cv.nguoi_nhan_id = nn.id " +
                                            "LEFT JOIN nhom_cong_viec ncv ON cv.nhom_id = ncv.id " +
                                            "LEFT JOIN cong_viec_tien_do cvtd ON cv.id = cvtd.cong_viec_id " +
                                            "WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (cv.ten_cong_viec LIKE ? OR cv.mo_ta LIKE ?)");
            String searchTerm = "%" + keyword.trim() + "%";
            params.add(searchTerm);
            params.add(searchTerm);
        }
        
        if (trangThai != null && !trangThai.trim().isEmpty() && !"all".equals(trangThai)) {
            sql.append(" AND cv.trang_thai = ?");
            params.add(trangThai);
        }
        
        if (mucDoUuTien != null && !mucDoUuTien.trim().isEmpty() && !"all".equals(mucDoUuTien)) {
            sql.append(" AND cv.muc_do_uu_tien = ?");
            params.add(mucDoUuTien);
        }
        
        sql.append(" GROUP BY cv.id ORDER BY cv.ngay_tao DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCongViec(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Đếm công việc theo trạng thái
    public int countCongViecByTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM cong_viec WHERE trang_thai = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Map ResultSet to CongViec object
    private CongViec mapResultSetToCongViec(ResultSet rs) throws SQLException {
        CongViec cv = new CongViec();
        cv.setId(rs.getInt("id"));
        cv.setTenCongViec(rs.getString("ten_cong_viec"));
        cv.setMoTa(rs.getString("mo_ta"));
        cv.setHanHoanThanh(rs.getDate("han_hoan_thanh"));
        cv.setMucDoUuTien(rs.getString("muc_do_uu_tien"));
        cv.setNguoiGiaoId(rs.getInt("nguoi_giao_id"));
        cv.setNguoiNhanId(rs.getInt("nguoi_nhan_id"));
        cv.setNhomId(rs.getInt("nhom_id"));
        cv.setTrangThai(rs.getString("trang_thai"));
        cv.setNgayTao(rs.getTimestamp("ngay_tao"));
        cv.setTenNguoiGiao(rs.getString("ten_nguoi_giao"));
        cv.setTenNguoiNhan(rs.getString("ten_nguoi_nhan"));
        cv.setTenNhom(rs.getString("ten_nhom"));
        cv.setPhanTramTienDo(rs.getInt("phan_tram_tien_do"));
        return cv;
    }
}
