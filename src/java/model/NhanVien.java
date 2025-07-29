package model;

import java.sql.Date;
import java.sql.Timestamp;

public class NhanVien {
    private int id;
    private String hoTen;
    private String email;
    private String matKhau;
    private String soDienThoai;
    private String gioiTinh;
    private Date ngaySinh;
    private int phongBanId;
    private String chucVu;
    private String trangThaiLamViec;
    private String vaiTro;
    private Date ngayVaoLam;
    private String avatarUrl;
    private Timestamp ngayTao;
    
    // Thông tin phòng ban (join)
    private String tenPhongBan;
    
    // Constructors
    public NhanVien() {}
    
    public NhanVien(String hoTen, String email, String matKhau, String soDienThoai, 
                    String gioiTinh, Date ngaySinh, int phongBanId, String chucVu, 
                    String trangThaiLamViec, String vaiTro, Date ngayVaoLam, String avatarUrl) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.phongBanId = phongBanId;
        this.chucVu = chucVu;
        this.trangThaiLamViec = trangThaiLamViec;
        this.vaiTro = vaiTro;
        this.ngayVaoLam = ngayVaoLam;
        this.avatarUrl = avatarUrl;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMatKhau() {
        return matKhau;
    }
    
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
    public String getGioiTinh() {
        return gioiTinh;
    }
    
    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    
    public Date getNgaySinh() {
        return ngaySinh;
    }
    
    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    
    public int getPhongBanId() {
        return phongBanId;
    }
    
    public void setPhongBanId(int phongBanId) {
        this.phongBanId = phongBanId;
    }
    
    public String getChucVu() {
        return chucVu;
    }
    
    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    
    public String getTrangThaiLamViec() {
        return trangThaiLamViec;
    }
    
    public void setTrangThaiLamViec(String trangThaiLamViec) {
        this.trangThaiLamViec = trangThaiLamViec;
    }
    
    public String getVaiTro() {
        return vaiTro;
    }
    
    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
    
    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }
    
    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getTenPhongBan() {
        return tenPhongBan;
    }
    
    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }
}
