package util;

import model.NhanVien;

public class AuthUtil {
    
    // Kiểm tra xem người dùng đã đăng nhập chưa (sử dụng Object để tránh servlet dependency)
    public static boolean isLoggedIn(Object session) {
        if (session == null) return false;
        try {
            // Sử dụng reflection để gọi getAttribute
            Object nhanvien = session.getClass().getMethod("getAttribute", String.class).invoke(session, "nhanvien");
            return nhanvien != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Lấy thông tin nhân viên hiện tại từ session
    public static NhanVien getCurrentUser(Object session) {
        if (session == null) return null;
        try {
            Object nhanvien = session.getClass().getMethod("getAttribute", String.class).invoke(session, "nhanvien");
            return (NhanVien) nhanvien;
        } catch (Exception e) {
            return null;
        }
    }
    
    // Kiểm tra vai trò của người dùng
    public static boolean hasRole(Object session, String role) {
        NhanVien nhanVien = getCurrentUser(session);
        return nhanVien != null && nhanVien.getVaiTro().equals(role);
    }
    
    // Kiểm tra quyền admin
    public static boolean isAdmin(Object session) {
        return hasRole(session, "admin");
    }
    
    // Kiểm tra quyền quản lý
    public static boolean isManager(Object session) {
        return hasRole(session, "quanly");
    }
    
    // Kiểm tra quyền nhân viên
    public static boolean isEmployee(Object session) {
        return hasRole(session, "nhanvien");
    }
    
    // Kiểm tra quyền admin hoặc quản lý
    public static boolean isAdminOrManager(Object session) {
        return isAdmin(session) || isManager(session);
    }
    
    // Lưu thông tin đăng nhập vào session
    public static void login(Object session, NhanVien nhanVien) {
        if (session == null) return;
        try {
            session.getClass().getMethod("setAttribute", String.class, Object.class).invoke(session, "nhanvien", nhanVien);
            session.getClass().getMethod("setAttribute", String.class, Object.class).invoke(session, "userId", nhanVien.getId());
            session.getClass().getMethod("setAttribute", String.class, Object.class).invoke(session, "userRole", nhanVien.getVaiTro());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Đăng xuất
    public static void logout(Object session) {
        if (session == null) return;
        try {
            session.getClass().getMethod("removeAttribute", String.class).invoke(session, "nhanvien");
            session.getClass().getMethod("removeAttribute", String.class).invoke(session, "userId");
            session.getClass().getMethod("removeAttribute", String.class).invoke(session, "userRole");
            session.getClass().getMethod("invalidate").invoke(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Lấy parameter từ request (sử dụng reflection để tránh servlet dependency)
    public static String getParameter(Object request, String name) {
        if (request == null) return null;
        try {
            Object value = request.getClass().getMethod("getParameter", String.class).invoke(request, name);
            return (String) value;
        } catch (Exception e) {
            return null;
        }
    }
    
    // Set attribute vào request (sử dụng reflection để tránh servlet dependency)
    public static void setAttribute(Object request, String name, Object value) {
        if (request == null) return;
        try {
            request.getClass().getMethod("setAttribute", String.class, Object.class).invoke(request, name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
