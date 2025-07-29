package model;

import java.sql.Timestamp;

public class KPI {
    private int id;
    private int nhanVienId;
    private int thang;
    private int nam;
    private String chiTieu;
    private String ketQua;
    private float diemKpi;
    private String ghiChu;
    private Timestamp ngayTao;
    
    // Join fields
    private String hoTenNhanVien;
    private String tenPhongBan;
    
    // Constructors
    public KPI() {}
    
    public KPI(int nhanVienId, int thang, int nam, String chiTieu, String ketQua, float diemKpi) {
        this.nhanVienId = nhanVienId;
        this.thang = thang;
        this.nam = nam;
        this.chiTieu = chiTieu;
        this.ketQua = ketQua;
        this.diemKpi = diemKpi;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getNhanVienId() {
        return nhanVienId;
    }
    
    public void setNhanVienId(int nhanVienId) {
        this.nhanVienId = nhanVienId;
    }
    
    public int getThang() {
        return thang;
    }
    
    public void setThang(int thang) {
        this.thang = thang;
    }
    
    public int getNam() {
        return nam;
    }
    
    public void setNam(int nam) {
        this.nam = nam;
    }
    
    public String getChiTieu() {
        return chiTieu;
    }
    
    public void setChiTieu(String chiTieu) {
        this.chiTieu = chiTieu;
    }
    
    public String getKetQua() {
        return ketQua;
    }
    
    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }
    
    public float getDiemKpi() {
        return diemKpi;
    }
    
    public void setDiemKpi(float diemKpi) {
        this.diemKpi = diemKpi;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getHoTenNhanVien() {
        return hoTenNhanVien;
    }
    
    public void setHoTenNhanVien(String hoTenNhanVien) {
        this.hoTenNhanVien = hoTenNhanVien;
    }
    
    public String getTenPhongBan() {
        return tenPhongBan;
    }
    
    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }
    
    // Helper methods
    public String getDanhGiaKPI() {
        if (diemKpi >= 9.0) return "Xuất sắc";
        if (diemKpi >= 8.0) return "Tốt";
        if (diemKpi >= 7.0) return "Đạt";
        if (diemKpi >= 5.0) return "Cần cải thiện";
        return "Không đạt";
    }
    
    public String getCssClassKPI() {
        if (diemKpi >= 9.0) return "badge-success";
        if (diemKpi >= 8.0) return "badge-primary";
        if (diemKpi >= 7.0) return "badge-info";
        if (diemKpi >= 5.0) return "badge-warning";
        return "badge-danger";
    }
}
