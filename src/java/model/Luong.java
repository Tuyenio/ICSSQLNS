package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Luong {
    private int id;
    private int nhanVienId;
    private int thang;
    private int nam;
    private BigDecimal luongCoBan;
    private BigDecimal phuCap;
    private BigDecimal thuong;
    private BigDecimal phat;
    private BigDecimal baoHiem;
    private BigDecimal thue;
    private BigDecimal luongThucTe;
    private String ghiChu;
    private String trangThai;
    private Date ngayTraLuong;
    private Timestamp ngayTao;
    
    // Join fields
    private String hoTenNhanVien;
    private String tenPhongBan;
    
    // Constructors
    public Luong() {}
    
    public Luong(int nhanVienId, int thang, int nam, BigDecimal luongCoBan) {
        this.nhanVienId = nhanVienId;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.phuCap = BigDecimal.ZERO;
        this.thuong = BigDecimal.ZERO;
        this.phat = BigDecimal.ZERO;
        this.baoHiem = BigDecimal.ZERO;
        this.thue = BigDecimal.ZERO;
        this.trangThai = "ChuaTra";
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
    
    public BigDecimal getLuongCoBan() {
        return luongCoBan;
    }
    
    public void setLuongCoBan(BigDecimal luongCoBan) {
        this.luongCoBan = luongCoBan;
    }
    
    public BigDecimal getPhuCap() {
        return phuCap;
    }
    
    public void setPhuCap(BigDecimal phuCap) {
        this.phuCap = phuCap;
    }
    
    public BigDecimal getThuong() {
        return thuong;
    }
    
    public void setThuong(BigDecimal thuong) {
        this.thuong = thuong;
    }
    
    public BigDecimal getPhat() {
        return phat;
    }
    
    public void setPhat(BigDecimal phat) {
        this.phat = phat;
    }
    
    public BigDecimal getBaoHiem() {
        return baoHiem;
    }
    
    public void setBaoHiem(BigDecimal baoHiem) {
        this.baoHiem = baoHiem;
    }
    
    public BigDecimal getThue() {
        return thue;
    }
    
    public void setThue(BigDecimal thue) {
        this.thue = thue;
    }
    
    public BigDecimal getLuongThucTe() {
        return luongThucTe;
    }
    
    public void setLuongThucTe(BigDecimal luongThucTe) {
        this.luongThucTe = luongThucTe;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public Date getNgayTraLuong() {
        return ngayTraLuong;
    }
    
    public void setNgayTraLuong(Date ngayTraLuong) {
        this.ngayTraLuong = ngayTraLuong;
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
    public BigDecimal tinhLuongThucTe() {
        BigDecimal tongThu = luongCoBan.add(phuCap).add(thuong);
        BigDecimal tongTru = phat.add(baoHiem).add(thue);
        return tongThu.subtract(tongTru);
    }
    
    public void capNhatLuongThucTe() {
        this.luongThucTe = tinhLuongThucTe();
    }
}
