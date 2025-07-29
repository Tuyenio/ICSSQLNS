package controller;

import dao.CongViecDAO;
import dao.NhanVienDAO;
import dao.ThongBaoDAO;
import dao.NhomCongViecDAO;
import model.CongViec;
import model.NhanVien;
import model.ThongBao;
import model.NhomCongViec;
import util.AuthUtil;
import util.DateUtil;
import util.ValidationUtil;
import java.util.List;

/**
 * Controller để xử lý các thao tác với công việc
 */
public class CongViecController {
    private CongViecDAO congViecDAO;
    private NhanVienDAO nhanVienDAO;
    private ThongBaoDAO thongBaoDAO;
    private NhomCongViecDAO nhomCongViecDAO;
    
    public CongViecController() {
        this.congViecDAO = new CongViecDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.thongBaoDAO = new ThongBaoDAO();
        this.nhomCongViecDAO = new NhomCongViecDAO();
    }
    
    // Xử lý yêu cầu chính
    public String processRequest(Object request, Object response, Object session) {
        try {
            // Kiểm tra đăng nhập
            if (!AuthUtil.isLoggedIn(session)) {
                return "redirect:login.jsp";
            }
            
            String action = getParameter(request, "action");
            
            switch (action != null ? action : "list") {
                case "list":
                    return listCongViec(request, session);
                case "add":
                    return showAddForm(request, session);
                case "create":
                    return createCongViec(request, session);
                case "edit":
                    return showEditForm(request, session);
                case "update":
                    return updateCongViec(request, session);
                case "delete":
                    return deleteCongViec(request, session);
                case "detail":
                    return showDetail(request, session);
                case "assign":
                    return assignTask(request, session);
                case "change_status":
                    return changeStatus(request, session);
                case "my_tasks":
                    return showMyTasks(request, session);
                case "search":
                    return searchTasks(request, session);
                default:
                    return listCongViec(request, session);
            }
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi hệ thống: " + e.getMessage());
            return "task.jsp";
        }
    }
    
    // Hiển thị danh sách công việc
    private String listCongViec(Object request, Object session) {
        try {
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            List<CongViec> congViecList;
            
            String status = getParameter(request, "status");
            String priority = getParameter(request, "priority");
            
            if (AuthUtil.isAdminOrManager(session)) {
                // Admin/Manager xem tất cả công việc
                if (ValidationUtil.isNotEmpty(status)) {
                    congViecList = congViecDAO.getCongViecByTrangThai(status);
                } else if (ValidationUtil.isNotEmpty(priority)) {
                    congViecList = congViecDAO.getCongViecByMucDoUuTien(priority);
                } else {
                    congViecList = congViecDAO.getAllCongViec();
                }
            } else {
                // Nhân viên chỉ xem công việc được giao
                congViecList = congViecDAO.getCongViecByNguoiThucHien(currentUser.getId());
            }
            
            // Thống kê công việc
            int totalTasks = congViecDAO.countCongViec();
            int completedTasks = congViecDAO.countCongViecByTrangThai("Hoan_thanh");
            int inProgressTasks = congViecDAO.countCongViecByTrangThai("Dang_thuc_hien");
            int overdueTasks = congViecDAO.countOverdueTasks();
            
            setAttribute(request, "congViecList", congViecList);
            setAttribute(request, "totalTasks", totalTasks);
            setAttribute(request, "completedTasks", completedTasks);
            setAttribute(request, "inProgressTasks", inProgressTasks);
            setAttribute(request, "overdueTasks", overdueTasks);
            setAttribute(request, "currentUser", currentUser);
            setAttribute(request, "selectedStatus", status);
            setAttribute(request, "selectedPriority", priority);
            
            return "task.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải danh sách công việc: " + e.getMessage());
            return "task.jsp";
        }
    }
    
    // Hiển thị form thêm công việc
    private String showAddForm(Object request, Object session) {
        try {
            // Chỉ admin/manager mới được tạo công việc
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền tạo công việc!");
                return listCongViec(request, session);
            }
            
            List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien();
            
            setAttribute(request, "nhanVienList", nhanVienList);
            setAttribute(request, "action", "create");
            setAttribute(request, "isEdit", false);
            
            return "task_form.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải form công việc: " + e.getMessage());
            return "task.jsp";
        }
    }
    
    // Tạo công việc mới
    private String createCongViec(Object request, Object session) {
        try {
            // Chỉ admin/manager mới được tạo công việc
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền tạo công việc!");
                return listCongViec(request, session);
            }
            
            CongViec congViec = buildCongViecFromRequest(request);
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            congViec.setNguoiGiaoId(currentUser.getId());
            
            // Validate
            String validation = validateCongViec(congViec);
            if (!validation.isEmpty()) {
                setAttribute(request, "error", validation);
                return showAddForm(request, session);
            }
            
            if (congViecDAO.addCongViec(congViec)) {
                // Gửi thông báo cho người được giao
                sendTaskNotification(congViec, "Công việc mới được giao", "CongViecMoi");
                
                setAttribute(request, "success", "Thêm công việc thành công!");
                return "redirect:CongViecController";
            } else {
                setAttribute(request, "error", "Không thể thêm công việc!");
                return showAddForm(request, session);
            }
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Cập nhật công việc
    private String updateCongViec(Object request, Object session) {
        try {
            CongViec congViec = buildCongViecFromRequest(request);
            congViec.setId(Integer.parseInt(getParameter(request, "id")));
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            
            // Kiểm tra quyền sửa
            CongViec existingTask = congViecDAO.getCongViecById(congViec.getId());
            if (!AuthUtil.isAdminOrManager(session) && 
                existingTask.getNguoiGiaoId() != currentUser.getId()) {
                setAttribute(request, "error", "Bạn không có quyền sửa công việc này!");
                return listCongViec(request, session);
            }
            
            // Validate
            String validation = validateCongViec(congViec);
            if (!validation.isEmpty()) {
                setAttribute(request, "error", validation);
                return showEditForm(request, session);
            }
            
            if (congViecDAO.updateCongViec(congViec)) {
                // Gửi thông báo nếu có thay đổi quan trọng
                if (existingTask.getNguoiThucHienId() != congViec.getNguoiThucHienId()) {
                    sendTaskNotification(congViec, "Công việc được giao lại", "CongViecCapNhat");
                }
                
                setAttribute(request, "success", "Cập nhật công việc thành công!");
                return "redirect:CongViecController";
            } else {
                setAttribute(request, "error", "Không thể cập nhật công việc!");
                return showEditForm(request, session);
            }
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return showEditForm(request, session);
        }
    }
    
    // Xóa công việc
    private String deleteCongViec(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            CongViec congViec = congViecDAO.getCongViecById(id);
            
            // Kiểm tra quyền xóa
            if (!AuthUtil.isAdmin(session) && 
                (congViec == null || congViec.getNguoiGiaoId() != currentUser.getId())) {
                setAttribute(request, "error", "Bạn không có quyền xóa công việc này!");
                return listCongViec(request, session);
            }
            
            if (congViecDAO.deleteCongViec(id)) {
                setAttribute(request, "success", "Xóa công việc thành công!");
            } else {
                setAttribute(request, "error", "Không thể xóa công việc!");
            }
            
            return "redirect:CongViecController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Hiển thị chi tiết công việc
    private String showDetail(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            CongViec congViec = congViecDAO.getCongViecById(id);
            
            if (congViec == null) {
                setAttribute(request, "error", "Không tìm thấy công việc!");
                return listCongViec(request, session);
            }
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            
            // Kiểm tra quyền xem
            if (!AuthUtil.isAdminOrManager(session) && 
                congViec.getNguoiThucHienId() != currentUser.getId() &&
                congViec.getNguoiGiaoId() != currentUser.getId()) {
                setAttribute(request, "error", "Bạn không có quyền xem công việc này!");
                return listCongViec(request, session);
            }
            
            setAttribute(request, "congViec", congViec);
            return "task_detail.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Giao công việc
    private String assignTask(Object request, Object session) {
        try {
            int taskId = Integer.parseInt(getParameter(request, "taskId"));
            int employeeId = Integer.parseInt(getParameter(request, "employeeId"));
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            CongViec congViec = congViecDAO.getCongViecById(taskId);
            
            // Kiểm tra quyền
            if (!AuthUtil.isAdminOrManager(session) && 
                (congViec == null || congViec.getNguoiGiaoId() != currentUser.getId())) {
                setAttribute(request, "error", "Bạn không có quyền giao công việc này!");
                return listCongViec(request, session);
            }
            
            if (congViecDAO.assignTask(taskId, employeeId)) {
                // Gửi thông báo
                congViec.setNguoiThucHienId(employeeId);
                sendTaskNotification(congViec, "Bạn được giao công việc mới", "CongViecGiao");
                
                setAttribute(request, "success", "Giao công việc thành công!");
            } else {
                setAttribute(request, "error", "Không thể giao công việc!");
            }
            
            return "redirect:CongViecController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Thay đổi trạng thái công việc
    private String changeStatus(Object request, Object session) {
        try {
            int taskId = Integer.parseInt(getParameter(request, "taskId"));
            String newStatus = getParameter(request, "status");
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            CongViec congViec = congViecDAO.getCongViecById(taskId);
            
            // Kiểm tra quyền
            if (!AuthUtil.isAdminOrManager(session) && 
                (congViec == null || congViec.getNguoiThucHienId() != currentUser.getId())) {
                setAttribute(request, "error", "Bạn không có quyền thay đổi trạng thái công việc này!");
                return listCongViec(request, session);
            }
            
            if (congViecDAO.updateStatus(taskId, newStatus)) {
                // Gửi thông báo cho người giao việc
                String message = "Trạng thái công việc '" + congViec.getTenCongViec() + "' đã được cập nhật: " + newStatus;
                sendStatusChangeNotification(congViec.getNguoiGiaoId(), message);
                
                setAttribute(request, "success", "Cập nhật trạng thái thành công!");
            } else {
                setAttribute(request, "error", "Không thể cập nhật trạng thái!");
            }
            
            return "redirect:CongViecController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Hiển thị công việc của tôi
    private String showMyTasks(Object request, Object session) {
        try {
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            
            String statusFilter = getParameter(request, "status");
            List<CongViec> myTasks;
            
            if (ValidationUtil.isNotEmpty(statusFilter)) {
                myTasks = congViecDAO.getCongViecByNguoiThucHienAndStatus(currentUser.getId(), statusFilter);
            } else {
                myTasks = congViecDAO.getCongViecByNguoiThucHien(currentUser.getId());
            }
            
            setAttribute(request, "myTasks", myTasks);
            setAttribute(request, "currentUser", currentUser);
            setAttribute(request, "selectedStatus", statusFilter);
            
            return "my_tasks.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải công việc của bạn: " + e.getMessage());
            return "task.jsp";
        }
    }
    
    // Tìm kiếm công việc
    private String searchTasks(Object request, Object session) {
        try {
            String keyword = getParameter(request, "keyword");
            String status = getParameter(request, "status");
            String priority = getParameter(request, "priority");
            String assigneeId = getParameter(request, "assigneeId");
            
            List<CongViec> searchResults = congViecDAO.searchCongViec(keyword, status, priority, 
                ValidationUtil.isNotEmpty(assigneeId) ? Integer.parseInt(assigneeId) : null);
            
            setAttribute(request, "congViecList", searchResults);
            setAttribute(request, "searchKeyword", keyword);
            setAttribute(request, "searchStatus", status);
            setAttribute(request, "searchPriority", priority);
            setAttribute(request, "searchAssigneeId", assigneeId);
            setAttribute(request, "isSearchResult", true);
            
            // Load employees for filter
            List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien();
            setAttribute(request, "nhanVienList", nhanVienList);
            
            return "task.jsp";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi tìm kiếm: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Build CongViec object from request
    private CongViec buildCongViecFromRequest(Object request) {
        CongViec congViec = new CongViec();
        
        congViec.setTenCongViec(getParameter(request, "tenCongViec"));
        congViec.setMoTa(getParameter(request, "moTa"));
        
        String nguoiThucHienIdStr = getParameter(request, "nguoiThucHienId");
        if (ValidationUtil.isNotEmpty(nguoiThucHienIdStr)) {
            congViec.setNguoiThucHienId(Integer.parseInt(nguoiThucHienIdStr));
        }
        
        String ngayBatDauStr = getParameter(request, "ngayBatDau");
        if (ValidationUtil.isNotEmpty(ngayBatDauStr)) {
            congViec.setNgayBatDau(new java.sql.Date(DateUtil.parseSqlDate(ngayBatDauStr).getTime()));
        }
        
        String ngayKetThucStr = getParameter(request, "ngayKetThuc");
        if (ValidationUtil.isNotEmpty(ngayKetThucStr)) {
            congViec.setNgayKetThuc(new java.sql.Date(DateUtil.parseSqlDate(ngayKetThucStr).getTime()));
        }
        
        congViec.setTrangThai(getParameter(request, "trangThai"));
        congViec.setMucDoUuTien(getParameter(request, "mucDoUuTien"));
        congViec.setGhiChu(getParameter(request, "ghiChu"));
        
        return congViec;
    }
    
    // Validate CongViec
    private String validateCongViec(CongViec congViec) {
        StringBuilder errors = new StringBuilder();
        
        if (ValidationUtil.isEmpty(congViec.getTenCongViec())) {
            errors.append("Vui lòng nhập tên công việc! ");
        }
        
        if (congViec.getNguoiThucHienId() <= 0) {
            errors.append("Vui lòng chọn người thực hiện! ");
        }
        
        if (congViec.getNgayBatDau() == null) {
            errors.append("Vui lòng chọn ngày bắt đầu! ");
        }
        
        if (congViec.getNgayKetThuc() == null) {
            errors.append("Vui lòng chọn ngày kết thúc! ");
        }
        
        if (congViec.getNgayBatDau() != null && congViec.getNgayKetThuc() != null &&
            congViec.getNgayBatDau().after(congViec.getNgayKetThuc())) {
            errors.append("Ngày bắt đầu phải trước ngày kết thúc! ");
        }
        
        if (ValidationUtil.isEmpty(congViec.getTrangThai())) {
            errors.append("Vui lòng chọn trạng thái! ");
        }
        
        return errors.toString().trim();
    }
    
    // Gửi thông báo công việc
    private void sendTaskNotification(CongViec congViec, String message, String type) {
        try {
            if (congViec.getNguoiThucHienId() > 0) {
                thongBaoDAO.guiThongBaoChoNhieuNguoi(
                    "Thông báo công việc",
                    message + ": " + congViec.getTenCongViec(),
                    type,
                    java.util.Arrays.asList(congViec.getNguoiThucHienId())
                );
            }
        } catch (Exception e) {
            System.err.println("Send task notification error: " + e.getMessage());
        }
    }
    
    // Gửi thông báo thay đổi trạng thái
    private void sendStatusChangeNotification(int userId, String message) {
        try {
            thongBaoDAO.guiThongBaoChoNhieuNguoi(
                "Cập nhật trạng thái công việc",
                message,
                "TrangThaiCapNhat",
                java.util.Arrays.asList(userId)
            );
        } catch (Exception e) {
            System.err.println("Send status change notification error: " + e.getMessage());
        }
    }
    
    // Helper methods để tránh dependency với servlet
    private String getParameter(Object request, String name) {
        try {
            return (String) request.getClass().getMethod("getParameter", String.class).invoke(request, name);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void setAttribute(Object request, String name, Object value) {
        try {
            request.getClass().getMethod("setAttribute", String.class, Object.class).invoke(request, name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Hiển thị form sửa công việc
    private String showEditForm(Object request, Object session) {
        try {
            int id = Integer.parseInt(getParameter(request, "id"));
            CongViec congViec = congViecDAO.getCongViecById(id);
            
            if (congViec == null) {
                setAttribute(request, "error", "Không tìm thấy công việc!");
                return listCongViec(request, session);
            }
            
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            
            // Kiểm tra quyền sửa
            if (!AuthUtil.isAdminOrManager(session) && 
                congViec.getNguoiGiaoId() != currentUser.getId()) {
                setAttribute(request, "error", "Bạn không có quyền sửa công việc này!");
                return listCongViec(request, session);
            }
            
            List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien();
            
            setAttribute(request, "nhanVienList", nhanVienList);
            setAttribute(request, "congViec", congViec);
            setAttribute(request, "action", "update");
            setAttribute(request, "isEdit", true);
            
            return "task_form.jsp";
            
        } catch (NumberFormatException e) {
            setAttribute(request, "error", "ID không hợp lệ!");
            return listCongViec(request, session);
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listCongViec(request, session);
        }
    }
    
    // Cập nhật công việc
    public boolean updateCongViec(CongViec congViec) {
        return congViecDAO.updateCongViec(congViec);
    }
    
    // Xóa công việc
    public boolean deleteCongViec(int id) {
        return congViecDAO.deleteCongViec(id);
    }
    
    // Lấy công việc theo nhân viên
    public List<CongViec> getCongViecByNhanVien(int nhanVienId) {
        return congViecDAO.getCongViecByNhanVien(nhanVienId);
    }
    
    // Lấy công việc theo trạng thái
    public List<CongViec> getCongViecByTrangThai(String trangThai) {
        return congViecDAO.getCongViecByTrangThai(trangThai);
    }
    
    // Lấy danh sách nhân viên cho dropdown
    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAllNhanVien();
    }
    
    // Lấy danh sách nhóm công việc cho dropdown
    public List<NhomCongViec> getAllNhomCongViec() {
        return nhomCongViecDAO.getAllNhomCongViec();
    }
    
    // Cập nhật trạng thái công việc
    public boolean updateTrangThaiCongViec(int id, String trangThaiMoi) {
        CongViec congViec = getCongViecById(id);
        if (congViec != null) {
            congViec.setTrangThai(trangThaiMoi);
            boolean success = updateCongViec(congViec);
            
            if (success) {
                // Gửi thông báo thay đổi trạng thái
                if ("DaHoanThanh".equals(trangThaiMoi)) {
                    sendCompletionNotification(congViec);
                } else if ("TreHan".equals(trangThaiMoi)) {
                    sendOverdueNotification(congViec);
                }
            }
            return success;
        }
        return false;
    }
    
    // Lấy thống kê công việc
    public List<CongViec> getAllCongViec() {
        return congViecDAO.getAllCongViec();
    }
    
    public CongViec getCongViecById(int id) {
        return congViecDAO.getCongViecById(id);
    }
    
    public int getTongCongViec() {
        return getAllCongViec().size();
    }
    
    public int getCongViecDaHoanThanh() {
        return getCongViecByTrangThai("DaHoanThanh").size();
    }
    
    public int getCongViecDangThucHien() {
        return getCongViecByTrangThai("DangThucHien").size();
    }
    
    public int getCongViecTreHan() {
        return getCongViecByTrangThai("TreHan").size();
    }
    
    public double getPhanTramHoanThanh() {
        int tong = getTongCongViec();
        if (tong == 0) return 0;
        return (double) getCongViecDaHoanThanh() / tong * 100;
    }
    
    // Gửi thông báo hoàn thành công việc
    private void sendCompletionNotification(CongViec congViec) {
        try {
            NhanVien nguoiNhan = nhanVienDAO.getNhanVienById(congViec.getNguoiNhanId());
            String tieuDe = "Công việc đã hoàn thành";
            String noiDung = String.format("Công việc '%s' đã được hoàn thành bởi %s", 
                                         congViec.getTenCongViec(), 
                                         nguoiNhan != null ? nguoiNhan.getHoTen() : "Nhân viên");
            
            ThongBao thongBao = new ThongBao(tieuDe, noiDung, congViec.getNguoiGiaoId(), "TaskMoi");
            thongBaoDAO.addThongBao(thongBao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Gửi thông báo công việc trễ hạn
    private void sendOverdueNotification(CongViec congViec) {
        try {
            String tieuDe = "Công việc trễ hạn";
            String noiDung = String.format("Công việc '%s' đã trễ hạn hoàn thành", congViec.getTenCongViec());
            
            // Gửi cho cả người giao và người nhận
            ThongBao thongBaoNguoiNhan = new ThongBao(tieuDe, noiDung, congViec.getNguoiNhanId(), "TreHan");
            ThongBao thongBaoNguoiGiao = new ThongBao(tieuDe, noiDung, congViec.getNguoiGiaoId(), "TreHan");
            
            thongBaoDAO.addThongBao(thongBaoNguoiNhan);
            thongBaoDAO.addThongBao(thongBaoNguoiGiao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Tìm kiếm công việc
    public List<CongViec> searchCongViec(String keyword, String trangThai, String mucDoUuTien) {
        List<CongViec> tatCaCongViec = getAllCongViec();
        
        return tatCaCongViec.stream()
                .filter(cv -> {
                    boolean matchKeyword = keyword == null || keyword.trim().isEmpty() || 
                                         cv.getTenCongViec().toLowerCase().contains(keyword.toLowerCase()) ||
                                         (cv.getMoTa() != null && cv.getMoTa().toLowerCase().contains(keyword.toLowerCase()));
                    
                    boolean matchTrangThai = trangThai == null || trangThai.trim().isEmpty() || 
                                           trangThai.equals(cv.getTrangThai());
                    
                    boolean matchMucDo = mucDoUuTien == null || mucDoUuTien.trim().isEmpty() || 
                                       mucDoUuTien.equals(cv.getMucDoUuTien());
                    
                    return matchKeyword && matchTrangThai && matchMucDo;
                })
                .toList();
    }
}
