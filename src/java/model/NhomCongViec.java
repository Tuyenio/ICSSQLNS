package model;

import java.sql.Timestamp;

public class NhomCongViec {
    private int id;
    private String tenNhom;
    private String moTa;
    private int nguoiTaoId;
    private Timestamp ngayTao;
    
    // Join fields
    private String tenNguoiTao;
    private int soThanhVien;
    private int soCongViec;
    
    // Constructors
    public NhomCongViec() {}
    
    public NhomCongViec(String tenNhom, String moTa, int nguoiTaoId) {
        this.tenNhom = tenNhom;
        this.moTa = moTa;
        this.nguoiTaoId = nguoiTaoId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTenNhom() {
        return tenNhom;
    }
    
    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public int getNguoiTaoId() {
        return nguoiTaoId;
    }
    
    public void setNguoiTaoId(int nguoiTaoId) {
        this.nguoiTaoId = nguoiTaoId;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getTenNguoiTao() {
        return tenNguoiTao;
    }
    
    public void setTenNguoiTao(String tenNguoiTao) {
        this.tenNguoiTao = tenNguoiTao;
    }
    
    public int getSoThanhVien() {
        return soThanhVien;
    }
    
    public void setSoThanhVien(int soThanhVien) {
        this.soThanhVien = soThanhVien;
    }
    
    public int getSoCongViec() {
        return soCongViec;
    }
    
    public void setSoCongViec(int soCongViec) {
        this.soCongViec = soCongViec;
    }
}
