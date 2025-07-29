package model;

import java.sql.Date;
import java.sql.Timestamp;

public class CongViec {
    private int id;
    private String tenCongViec;
    private String moTa;
    private Date hanHoanThanh;
    private String mucDoUuTien;
    private int nguoiGiaoId;
    private int nguoiNhanId;
    private int nhomId;
    private String trangThai;
    private Timestamp ngayTao;
    
    // Thông tin join
    private String tenNguoiGiao;
    private String tenNguoiNhan;
    private String tenNhom;
    private int phanTramTienDo;
    
    // Constructors
    public CongViec() {}
    
    public CongViec(String tenCongViec, String moTa, Date hanHoanThanh, String mucDoUuTien,
                    int nguoiGiaoId, int nguoiNhanId, int nhomId, String trangThai) {
        this.tenCongViec = tenCongViec;
        this.moTa = moTa;
        this.hanHoanThanh = hanHoanThanh;
        this.mucDoUuTien = mucDoUuTien;
        this.nguoiGiaoId = nguoiGiaoId;
        this.nguoiNhanId = nguoiNhanId;
        this.nhomId = nhomId;
        this.trangThai = trangThai;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTenCongViec() {
        return tenCongViec;
    }
    
    public void setTenCongViec(String tenCongViec) {
        this.tenCongViec = tenCongViec;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public Date getHanHoanThanh() {
        return hanHoanThanh;
    }
    
    public void setHanHoanThanh(Date hanHoanThanh) {
        this.hanHoanThanh = hanHoanThanh;
    }
    
    public String getMucDoUuTien() {
        return mucDoUuTien;
    }
    
    public void setMucDoUuTien(String mucDoUuTien) {
        this.mucDoUuTien = mucDoUuTien;
    }
    
    public int getNguoiGiaoId() {
        return nguoiGiaoId;
    }
    
    public void setNguoiGiaoId(int nguoiGiaoId) {
        this.nguoiGiaoId = nguoiGiaoId;
    }
    
    public int getNguoiNhanId() {
        return nguoiNhanId;
    }
    
    public void setNguoiNhanId(int nguoiNhanId) {
        this.nguoiNhanId = nguoiNhanId;
    }
    
    public int getNhomId() {
        return nhomId;
    }
    
    public void setNhomId(int nhomId) {
        this.nhomId = nhomId;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getTenNguoiGiao() {
        return tenNguoiGiao;
    }
    
    public void setTenNguoiGiao(String tenNguoiGiao) {
        this.tenNguoiGiao = tenNguoiGiao;
    }
    
    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }
    
    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }
    
    public String getTenNhom() {
        return tenNhom;
    }
    
    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }
    
    public int getPhanTramTienDo() {
        return phanTramTienDo;
    }
    
    public void setPhanTramTienDo(int phanTramTienDo) {
        this.phanTramTienDo = phanTramTienDo;
    }
    
    // Additional methods for JSP compatibility
    public String getNguoiNhan() {
        return this.tenNguoiNhan; // Return name of assignee
    }
    
    public Date getNgayBatDau() {
        return this.ngayTao != null ? new Date(this.ngayTao.getTime()) : null; // Use creation date as start date
    }
    
    public Date getNgayKetThuc() {
        return this.hanHoanThanh; // Use deadline as end date
    }
    
    // Additional setter methods for form handling
    public void setNguoiGiao(int nguoiGiaoId) {
        this.nguoiGiaoId = nguoiGiaoId;
    }
    
    public void setNguoiNhan(int nguoiNhanId) {
        this.nguoiNhanId = nguoiNhanId;
    }
    
    public void setNgayBatDau(Date ngayBatDau) {
        // Set creation date based on start date
        if (ngayBatDau != null) {
            this.ngayTao = new Timestamp(ngayBatDau.getTime());
        }
    }
    
    public void setNgayKetThuc(Date ngayKetThuc) {
        this.hanHoanThanh = ngayKetThuc;
    }
}
