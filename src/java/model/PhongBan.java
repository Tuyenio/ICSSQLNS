package model;

import java.sql.Timestamp;

public class PhongBan {
    private int id;
    private String tenPhong;
    private int truongPhongId;
    private Timestamp ngayTao;
    private String moTa;
    private String dienThoai;
    private String email;
    
    // Thông tin join
    private String tenTruongPhong;
    private int soNhanVien;
    
    // Constructors
    public PhongBan() {}
    
    public PhongBan(String tenPhong, int truongPhongId) {
        this.tenPhong = tenPhong;
        this.truongPhongId = truongPhongId;
    }
    
    public PhongBan(String tenPhong, int truongPhongId, String moTa, String dienThoai, String email) {
        this.tenPhong = tenPhong;
        this.truongPhongId = truongPhongId;
        this.moTa = moTa;
        this.dienThoai = dienThoai;
        this.email = email;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTenPhong() {
        return tenPhong;
    }
    
    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
    
    public int getTruongPhongId() {
        return truongPhongId;
    }
    
    public void setTruongPhongId(int truongPhongId) {
        this.truongPhongId = truongPhongId;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getTenTruongPhong() {
        return tenTruongPhong;
    }
    
    public void setTenTruongPhong(String tenTruongPhong) {
        this.tenTruongPhong = tenTruongPhong;
    }
    
    public int getSoNhanVien() {
        return soNhanVien;
    }
    
    public void setSoNhanVien(int soNhanVien) {
        this.soNhanVien = soNhanVien;
    }
    
    // Alias methods for JSP compatibility
    public String getTenPhongBan() {
        return tenPhong;
    }
    
    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhong = tenPhongBan;
    }
    
    public String getTruongPhong() {
        return tenTruongPhong;
    }
    
    public void setTruongPhong(String truongPhong) {
        this.tenTruongPhong = truongPhong;
    }
    
    public int getSoLuongNhanVien() {
        return soNhanVien;
    }
    
    public void setSoLuongNhanVien(int soLuongNhanVien) {
        this.soNhanVien = soLuongNhanVien;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public String getDienThoai() {
        return dienThoai;
    }
    
    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
