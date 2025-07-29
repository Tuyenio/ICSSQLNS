package model;

public class NhomThanhVien {
    private int id;
    private int nhomId;
    private int nhanVienId;
    private String vaiTroNhom;
    
    // Join fields
    private String tenNhom;
    private String tenNhanVien;
    private String emailNhanVien;
    private String chucVu;
    
    // Constructors
    public NhomThanhVien() {}
    
    public NhomThanhVien(int nhomId, int nhanVienId, String vaiTroNhom) {
        this.nhomId = nhomId;
        this.nhanVienId = nhanVienId;
        this.vaiTroNhom = vaiTroNhom;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getNhomId() {
        return nhomId;
    }
    
    public void setNhomId(int nhomId) {
        this.nhomId = nhomId;
    }
    
    public int getNhanVienId() {
        return nhanVienId;
    }
    
    public void setNhanVienId(int nhanVienId) {
        this.nhanVienId = nhanVienId;
    }
    
    public String getVaiTroNhom() {
        return vaiTroNhom;
    }
    
    public void setVaiTroNhom(String vaiTroNhom) {
        this.vaiTroNhom = vaiTroNhom;
    }
    
    public String getTenNhom() {
        return tenNhom;
    }
    
    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }
    
    public String getTenNhanVien() {
        return tenNhanVien;
    }
    
    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }
    
    public String getEmailNhanVien() {
        return emailNhanVien;
    }
    
    public void setEmailNhanVien(String emailNhanVien) {
        this.emailNhanVien = emailNhanVien;
    }
    
    public String getChucVu() {
        return chucVu;
    }
    
    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}
