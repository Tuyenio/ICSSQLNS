package controller;

import dao.ThongBaoDAO;
import dao.NhanVienDAO;
import dao.PhongBanDAO;
import model.ThongBao;
import model.NhanVien;
import model.PhongBan;
import util.AuthUtil;
import java.util.List;

public class ThongBaoController {
    
    private ThongBaoDAO thongBaoDAO;
    private NhanVienDAO nhanVienDAO;
    private PhongBanDAO phongBanDAO;
    
    public ThongBaoController() {
        this.thongBaoDAO = new ThongBaoDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.phongBanDAO = new PhongBanDAO();
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
                    return listThongBao(request, session);
                case "add":
                    return showAddForm(request, session);
                case "create":
                    return createThongBao(request, session);
                case "read":
                    return readThongBao(request, session);
                case "delete":
                    return deleteThongBao(request, session);
                case "send_to_all":
                    return sendToAll(request, session);
                case "send_to_department":
                    return sendToDepartment(request, session);
                case "unread_count":
                    return getUnreadCount(request, session);
                default:
                    return listThongBao(request, session);
            }
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi hệ thống: " + e.getMessage());
            return "notification.jsp";
        }
    }
    
    // Hiển thị danh sách thông báo
    private String listThongBao(Object request, Object session) {
        try {
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            List<ThongBao> thongBaoList;
            
            if (AuthUtil.isAdminOrManager(session)) {
                // Admin/Manager xem tất cả thông báo
                thongBaoList = thongBaoDAO.getAllThongBao();
            } else {
                // Nhân viên chỉ xem thông báo của mình
                thongBaoList = thongBaoDAO.getThongBaoByNguoiNhan(currentUser.getId());
            }
            
            // Lấy số thông báo chưa đọc
            int unreadCount = thongBaoDAO.countThongBaoChuaDoc(currentUser.getId());
            
            setAttribute(request, "thongBaoList", thongBaoList);
            setAttribute(request, "unreadCount", unreadCount);
            setAttribute(request, "currentUser", currentUser);
            
            return "notification.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải danh sách thông báo: " + e.getMessage());
            return "notification.jsp";
        }
    }
    
    // Hiển thị form thêm thông báo
    private String showAddForm(Object request, Object session) {
        try {
            // Chỉ admin/manager mới được tạo thông báo
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền tạo thông báo!");
                return listThongBao(request, session);
            }
            
            List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien();
            List<PhongBan> phongBanList = phongBanDAO.getAllPhongBan();
            
            setAttribute(request, "nhanVienList", nhanVienList);
            setAttribute(request, "phongBanList", phongBanList);
            setAttribute(request, "action", "create");
            
            return "notification_form.jsp";
        } catch (Exception e) {
            setAttribute(request, "error", "Không thể tải form thông báo: " + e.getMessage());
            return "notification.jsp";
        }
    }
    
    // Tạo thông báo mới
    private String createThongBao(Object request, Object session) {
        try {
            // Chỉ admin/manager mới được tạo thông báo
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền tạo thông báo!");
                return listThongBao(request, session);
            }
            
            String tieuDe = getParameter(request, "tieuDe");
            String noiDung = getParameter(request, "noiDung");
            String loaiThongBao = getParameter(request, "loaiThongBao");
            String sendType = getParameter(request, "sendType");
            
            // Validate
            if (tieuDe == null || tieuDe.trim().isEmpty()) {
                setAttribute(request, "error", "Vui lòng nhập tiêu đề!");
                return showAddForm(request, session);
            }
            
            if (noiDung == null || noiDung.trim().isEmpty()) {
                setAttribute(request, "error", "Vui lòng nhập nội dung!");
                return showAddForm(request, session);
            }
            
            boolean success = false;
            
            switch (sendType) {
                case "all":
                    success = thongBaoDAO.guiThongBaoToAll(tieuDe, noiDung, loaiThongBao);
                    break;
                case "department":
                    String phongBanIdStr = getParameter(request, "phongBanId");
                    if (phongBanIdStr != null && !phongBanIdStr.isEmpty()) {
                        int phongBanId = Integer.parseInt(phongBanIdStr);
                        success = thongBaoDAO.guiThongBaoToPhongBan(phongBanId, tieuDe, noiDung, loaiThongBao);
                    } else {
                        setAttribute(request, "error", "Vui lòng chọn phòng ban!");
                        return showAddForm(request, session);
                    }
                    break;
                case "individual":
                    String nguoiNhanIdStr = getParameter(request, "nguoiNhanId");
                    if (nguoiNhanIdStr != null && !nguoiNhanIdStr.isEmpty()) {
                        int nguoiNhanId = Integer.parseInt(nguoiNhanIdStr);
                        ThongBao thongBao = new ThongBao();
                        thongBao.setNguoiNhanId(nguoiNhanId);
                        thongBao.setTieuDe(tieuDe);
                        thongBao.setNoiDung(noiDung);
                        thongBao.setLoaiThongBao(loaiThongBao);
                        thongBao.setDaDoc(false);
                        success = thongBaoDAO.addThongBao(thongBao);
                    } else {
                        setAttribute(request, "error", "Vui lòng chọn người nhận!");
                        return showAddForm(request, session);
                    }
                    break;
                default:
                    setAttribute(request, "error", "Loại gửi không hợp lệ!");
                    return showAddForm(request, session);
            }
            
            if (success) {
                setAttribute(request, "success", "Gửi thông báo thành công!");
                return "redirect:ThongBaoController";
            } else {
                setAttribute(request, "error", "Không thể gửi thông báo!");
                return showAddForm(request, session);
            }
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return showAddForm(request, session);
        }
    }
    
    // Đọc thông báo
    private String readThongBao(Object request, Object session) {
        try {
            String idStr = getParameter(request, "id");
            if (idStr == null || idStr.isEmpty()) {
                setAttribute(request, "error", "ID thông báo không hợp lệ!");
                return listThongBao(request, session);
            }
            
            int id = Integer.parseInt(idStr);
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            
            ThongBao thongBao = thongBaoDAO.getThongBaoById(id);
            if (thongBao == null) {
                setAttribute(request, "error", "Không tìm thấy thông báo!");
                return listThongBao(request, session);
            }
            
            // Kiểm tra quyền đọc
            if (!AuthUtil.isAdminOrManager(session) && thongBao.getNguoiNhanId() != currentUser.getId()) {
                setAttribute(request, "error", "Bạn không có quyền đọc thông báo này!");
                return listThongBao(request, session);
            }
            
            // Đánh dấu đã đọc
            if (!thongBao.isDaDoc() && thongBao.getNguoiNhanId() == currentUser.getId()) {
                thongBaoDAO.markAsRead(id);
            }
            
            setAttribute(request, "thongBao", thongBao);
            return "notification_detail.jsp";
            
        } catch (NumberFormatException e) {
            setAttribute(request, "error", "ID thông báo không hợp lệ!");
            return listThongBao(request, session);
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listThongBao(request, session);
        }
    }
    
    // Xóa thông báo
    private String deleteThongBao(Object request, Object session) {
        try {
            // Chỉ admin mới được xóa thông báo
            if (!AuthUtil.isAdmin(session)) {
                setAttribute(request, "error", "Bạn không có quyền xóa thông báo!");
                return listThongBao(request, session);
            }
            
            String idStr = getParameter(request, "id");
            if (idStr == null || idStr.isEmpty()) {
                setAttribute(request, "error", "ID thông báo không hợp lệ!");
                return listThongBao(request, session);
            }
            
            int id = Integer.parseInt(idStr);
            
            if (thongBaoDAO.deleteThongBao(id)) {
                setAttribute(request, "success", "Xóa thông báo thành công!");
            } else {
                setAttribute(request, "error", "Không thể xóa thông báo!");
            }
            
            return "redirect:ThongBaoController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listThongBao(request, session);
        }
    }
    
    // Gửi thông báo đến tất cả
    private String sendToAll(Object request, Object session) {
        try {
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền gửi thông báo!");
                return listThongBao(request, session);
            }
            
            String tieuDe = getParameter(request, "tieuDe");
            String noiDung = getParameter(request, "noiDung");
            String loaiThongBao = getParameter(request, "loaiThongBao");
            
            if (thongBaoDAO.guiThongBaoToAll(tieuDe, noiDung, loaiThongBao)) {
                setAttribute(request, "success", "Gửi thông báo đến tất cả thành công!");
            } else {
                setAttribute(request, "error", "Không thể gửi thông báo!");
            }
            
            return "redirect:ThongBaoController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listThongBao(request, session);
        }
    }
    
    // Gửi thông báo đến phòng ban
    private String sendToDepartment(Object request, Object session) {
        try {
            if (!AuthUtil.isAdminOrManager(session)) {
                setAttribute(request, "error", "Bạn không có quyền gửi thông báo!");
                return listThongBao(request, session);
            }
            
            String phongBanIdStr = getParameter(request, "phongBanId");
            String tieuDe = getParameter(request, "tieuDe");
            String noiDung = getParameter(request, "noiDung");
            String loaiThongBao = getParameter(request, "loaiThongBao");
            
            int phongBanId = Integer.parseInt(phongBanIdStr);
            
            if (thongBaoDAO.guiThongBaoToPhongBan(phongBanId, tieuDe, noiDung, loaiThongBao)) {
                setAttribute(request, "success", "Gửi thông báo đến phòng ban thành công!");
            } else {
                setAttribute(request, "error", "Không thể gửi thông báo!");
            }
            
            return "redirect:ThongBaoController";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return listThongBao(request, session);
        }
    }
    
    // Lấy số thông báo chưa đọc
    private String getUnreadCount(Object request, Object session) {
        try {
            NhanVien currentUser = AuthUtil.getCurrentUser(session);
            int unreadCount = thongBaoDAO.countThongBaoChuaDoc(currentUser.getId());
            
            setAttribute(request, "unreadCount", unreadCount);
            return "json";
            
        } catch (Exception e) {
            setAttribute(request, "error", "Lỗi: " + e.getMessage());
            return "json";
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
}
