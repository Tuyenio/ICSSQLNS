package model;

import java.sql.Timestamp;

public class ThongBao {
    private int id;
    private String tieuDe;
    private String noiDung;
    private int nguoiNhanId;
    private String loaiThongBao;
    private boolean daDoc;
    private Timestamp ngayTao;
    
    // Join fields
    private String hoTenNguoiNhan;
    
    // Constructors
    public ThongBao() {}
    
    public ThongBao(String tieuDe, String noiDung, int nguoiNhanId, String loaiThongBao) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.nguoiNhanId = nguoiNhanId;
        this.loaiThongBao = loaiThongBao;
        this.daDoc = false;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTieuDe() {
        return tieuDe;
    }
    
    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
    
    public String getNoiDung() {
        return noiDung;
    }
    
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    
    public int getNguoiNhanId() {
        return nguoiNhanId;
    }
    
    public void setNguoiNhanId(int nguoiNhanId) {
        this.nguoiNhanId = nguoiNhanId;
    }
    
    public String getLoaiThongBao() {
        return loaiThongBao;
    }
    
    public void setLoaiThongBao(String loaiThongBao) {
        this.loaiThongBao = loaiThongBao;
    }
    
    public boolean isDaDoc() {
        return daDoc;
    }
    
    public void setDaDoc(boolean daDoc) {
        this.daDoc = daDoc;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public String getHoTenNguoiNhan() {
        return hoTenNguoiNhan;
    }
    
    public void setHoTenNguoiNhan(String hoTenNguoiNhan) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
    }
    
    // Helper methods
    public String getLoaiThongBaoText() {
        switch (loaiThongBao) {
            case "TaskMoi": return "Công việc mới";
            case "Deadline": return "Hạn chót";
            case "TreHan": return "Trễ hạn";
            case "Luong": return "Lương";
            default: return "Khác";
        }
    }
    
    public String getTrangThaiDoc() {
        return daDoc ? "Đã đọc" : "Chưa đọc";
    }
}
