package controller;

import dao.ChamCongDAO;
import dao.NhanVienDAO;
import model.ChamCong;
import model.NhanVien;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Calendar;

/**
 * Controller để xử lý các thao tác chấm công
 */
public class ChamCongController {
    private ChamCongDAO chamCongDAO;
    private NhanVienDAO nhanVienDAO;
    
    public ChamCongController() {
        this.chamCongDAO = new ChamCongDAO();
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    // Lấy tất cả bản ghi chấm công
    public List<ChamCong> getAllChamCong() {
        return chamCongDAO.getAllChamCong();
    }
    
    // Lấy chấm công theo nhân viên và tháng
    public List<ChamCong> getChamCongByNhanVienAndMonth(int nhanVienId, int thang, int nam) {
        return chamCongDAO.getChamCongByNhanVienAndMonth(nhanVienId, thang, nam);
    }
    
    // Check-in cho nhân viên
    public boolean checkIn(int nhanVienId) {
        Date ngayHienTai = new Date(System.currentTimeMillis());
        Time gioHienTai = new Time(System.currentTimeMillis());
        
        // Kiểm tra nhân viên có tồn tại không
        NhanVien nhanVien = nhanVienDAO.getNhanVienById(nhanVienId);
        if (nhanVien == null) {
            return false;
        }
        
        // Kiểm tra đã check-in chưa
        if (chamCongDAO.hasCheckedIn(nhanVienId, ngayHienTai)) {
            return false; // Đã check-in rồi
        }
        
        return chamCongDAO.checkIn(nhanVienId, ngayHienTai, gioHienTai);
    }
    
    // Check-out cho nhân viên
    public boolean checkOut(int nhanVienId) {
        Date ngayHienTai = new Date(System.currentTimeMillis());
        Time gioHienTai = new Time(System.currentTimeMillis());
        
        // Kiểm tra đã check-in chưa
        if (!chamCongDAO.hasCheckedIn(nhanVienId, ngayHienTai)) {
            return false; // Chưa check-in
        }
        
        // Kiểm tra đã check-out chưa
        if (chamCongDAO.hasCheckedOut(nhanVienId, ngayHienTai)) {
            return false; // Đã check-out rồi
        }
        
        return chamCongDAO.checkOut(nhanVienId, ngayHienTai, gioHienTai);
    }
    
    // Lấy trạng thái chấm công hôm nay
    public ChamCong getChamCongToday(int nhanVienId) {
        Date ngayHienTai = new Date(System.currentTimeMillis());
        return chamCongDAO.getChamCongToday(nhanVienId, ngayHienTai);
    }
    
    // Kiểm tra có thể check-in không
    public boolean canCheckIn(int nhanVienId) {
        Date ngayHienTai = new Date(System.currentTimeMillis());
        return !chamCongDAO.hasCheckedIn(nhanVienId, ngayHienTai);
    }
    
    // Kiểm tra có thể check-out không
    public boolean canCheckOut(int nhanVienId) {
        Date ngayHienTai = new Date(System.currentTimeMillis());
        return chamCongDAO.hasCheckedIn(nhanVienId, ngayHienTai) && 
               !chamCongDAO.hasCheckedOut(nhanVienId, ngayHienTai);
    }
    
    // Lấy thống kê chấm công theo tháng
    public int getThongKeChamCongThang(int thang, int nam) {
        return chamCongDAO.countChamCongByMonth(thang, nam);
    }
    
    // Lấy thống kê chấm công tháng hiện tại
    public int getThongKeChamCongThangHienTai() {
        Calendar cal = Calendar.getInstance();
        int thang = cal.get(Calendar.MONTH) + 1;
        int nam = cal.get(Calendar.YEAR);
        return getThongKeChamCongThang(thang, nam);
    }
    
    // Lấy lịch sử chấm công của nhân viên theo tháng hiện tại
    public List<ChamCong> getLichSuChamCongThangHienTai(int nhanVienId) {
        Calendar cal = Calendar.getInstance();
        int thang = cal.get(Calendar.MONTH) + 1;
        int nam = cal.get(Calendar.YEAR);
        return getChamCongByNhanVienAndMonth(nhanVienId, thang, nam);
    }
    
    // Validate thời gian làm việc (8h-17h30)
    public boolean isValidWorkingTime() {
        Calendar cal = Calendar.getInstance();
        int gio = cal.get(Calendar.HOUR_OF_DAY);
        
        // Cho phép check-in từ 7h30 đến 9h
        // Cho phép check-out từ 17h đến 19h
        if (gio >= 7 && gio <= 9) return true; // Check-in time
        if (gio >= 17 && gio <= 19) return true; // Check-out time
        if (gio > 9 && gio < 17) return true; // Working hours
        
        return false;
    }
    
    // Tính số giờ làm việc
    public double calculateWorkingHours(Time checkIn, Time checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        
        long diff = checkOut.getTime() - checkIn.getTime();
        return diff / (1000.0 * 60 * 60); // Convert to hours
    }
}
