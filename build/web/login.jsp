<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.NhanVienDAO" %>
<%@ page import="model.NhanVien" %>
<%@ page import="util.AuthUtil" %>

<%
    String errorMsg = (String) request.getAttribute("errorMsg");
    String successMsg = (String) request.getAttribute("successMsg");
    
    // Kiểm tra nếu đã đăng nhập rồi thì chuyển hướng
    if (AuthUtil.isLoggedIn(session)) {
        NhanVien currentUser = AuthUtil.getCurrentUser(session);
        if (currentUser != null) {
            if (AuthUtil.isAdmin(session) || AuthUtil.isManager(session)) {
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("user_dashboard.jsp");
            }
            return;
        }
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ thống QLNS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            width: 100%;
            max-width: 400px;
            padding: 40px;
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .login-header img {
            width: 80px;
            height: 80px;
            margin-bottom: 20px;
        }
        
        .login-header h2 {
            color: #333;
            font-weight: 700;
            margin-bottom: 10px;
        }
        
        .login-header p {
            color: #666;
            font-size: 14px;
        }
        
        .form-floating {
            margin-bottom: 20px;
        }
        
        .form-floating input {
            border: 2px solid #e9ecef;
            border-radius: 12px;
            padding: 12px 16px;
            font-size: 16px;
            transition: all 0.3s ease;
        }
        
        .form-floating input:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .form-floating label {
            padding: 1rem 16px;
            color: #666;
        }
        
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 12px;
            padding: 12px;
            font-size: 16px;
            font-weight: 600;
            color: white;
            width: 100%;
            transition: transform 0.3s ease;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            color: white;
        }
        
        .alert {
            border-radius: 12px;
            border: none;
            margin-bottom: 20px;
        }
        
        .demo-accounts {
            background: #f8f9fa;
            border-radius: 12px;
            padding: 20px;
            margin-top: 20px;
        }
        
        .demo-accounts h6 {
            color: #495057;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .demo-account {
            background: white;
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 10px;
            border-left: 4px solid #667eea;
        }
        
        .demo-account:last-child {
            margin-bottom: 0;
        }
        
        .demo-account small {
            color: #666;
            font-size: 12px;
        }
        
        .demo-account strong {
            color: #333;
            font-size: 13px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <img src="Img/logo.png" alt="Logo" onerror="this.style.display='none'">
            <h2>Đăng nhập</h2>
            <p>Hệ thống Quản lý Nhân sự</p>
        </div>
        
        <% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <%= errorMsg %>
            </div>
        <% } %>
        
        <% if (successMsg != null && !successMsg.trim().isEmpty()) { %>
            <div class="alert alert-success">
                <i class="fas fa-check-circle me-2"></i>
                <%= successMsg %>
            </div>
        <% } %>
        
        <form action="login_handler.jsp" method="post">
            <input type="hidden" name="action" value="login">
            
            <div class="form-floating">
                <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                <label for="email"><i class="fas fa-envelope me-2"></i>Email</label>
            </div>
            
            <div class="form-floating">
                <input type="password" class="form-control" id="matKhau" name="matKhau" placeholder="Mật khẩu" required>
                <label for="matKhau"><i class="fas fa-lock me-2"></i>Mật khẩu</label>
            </div>
            
            <button type="submit" class="btn btn-login">
                <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
            </button>
        </form>
        
        <div class="demo-accounts">
            <h6><i class="fas fa-info-circle me-2"></i>Tài khoản demo:</h6>
            
            <div class="demo-account">
                <strong>Admin:</strong> minh@company.com<br>
                <small>Mật khẩu: password123</small>
            </div>
            
            <div class="demo-account">
                <strong>Manager:</strong> lan@company.com<br>
                <small>Mật khẩu: password123</small>
            </div>
            
            <div class="demo-account">
                <strong>Nhân viên:</strong> cuong@company.com<br>
                <small>Mật khẩu: password123</small>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
