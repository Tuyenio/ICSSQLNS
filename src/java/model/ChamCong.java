package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ChamCong {
    private int id;
    private int nhanVienId;
    private Date ngay;
    private Time checkIn;
    private Time checkOut;
    private Timestamp ngayTao;
    
    // Join fields
    private String hoTenNhanVien;
    private String tenPhongBan;
    
    // Constructors
    public ChamCong() {}
    
    public ChamCong(int nhanVienId, Date ngay, Time checkIn, Time checkOut) {
        this.nhanVienId = nhanVienId;
        this.ngay = ngay;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
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
    
    public Date getNgay() {
        return ngay;
    }
    
    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }
    
    public Time getCheckIn() {
        return checkIn;
    }
    
    public void setCheckIn(Time checkIn) {
        this.checkIn = checkIn;
    }
    
    public Time getCheckOut() {
        return checkOut;
    }
    
    public void setCheckOut(Time checkOut) {
        this.checkOut = checkOut;
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
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    // Helper methods
    public String getTrangThaiChamCong() {
        if (checkIn == null) return "Chưa vào";
        if (checkOut == null) return "Chưa ra";
        return "Đầy đủ";
    }
    
    public long getSoGioLam() {
        if (checkIn == null || checkOut == null) return 0;
        return (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60);
    }
}
