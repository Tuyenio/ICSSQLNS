<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.ChamCongDAO" %>
<%@ page import="dao.NhanVienDAO" %>
<%@ page import="dao.LuongDAO" %>
<%@ page import="model.ChamCong" %>
<%@ page import="model.NhanVien" %>
<%@ page import="model.Luong" %>
<%@ page import="util.AuthUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    // Kiểm tra đăng nhập
    if (!AuthUtil.isLoggedIn(session)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Lấy dữ liệu chấm công
    ChamCongDAO chamCongDAO = new ChamCongDAO();
    LuongDAO luongDAO = new LuongDAO();
    NhanVienDAO nhanVienDAO = new NhanVienDAO();
    
    // Lấy tháng năm hiện tại
    Calendar cal = Calendar.getInstance();
    int thangHienTai = cal.get(Calendar.MONTH) + 1;
    int namHienTai = cal.get(Calendar.YEAR);
    
    // Lấy tham số filter
    String thangParam = request.getParameter("thang");
    String namParam = request.getParameter("nam");
    
    int thang = thangParam != null ? Integer.parseInt(thangParam) : thangHienTai;
    int nam = namParam != null ? Integer.parseInt(namParam) : namHienTai;
    
    // Lấy danh sách chấm công và lương
    List<ChamCong> dsChamCong = chamCongDAO.getChamCongByMonth(thang, nam);
    List<Luong> dsLuong = luongDAO.getLuongByMonth(thang, nam);
    List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
%>

    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <title>Chấm công & Lương</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            var PAGE_TITLE = '<i class="fa-solid fa-calendar-check me-2"></i>Chấm công & Lương';
        </script>
        <style>
            body {
                background: #f4f6fa;
            }

            .sidebar {
                min-height: 100vh;
                background: linear-gradient(180deg, #23272f 0%, #343a40 100%);
                color: #fff;
                width: 240px;
                transition: width 0.2s;
                box-shadow: 2px 0 8px #0001;
                z-index: 10;
                position: fixed;
                top: 0;
                left: 0;
                bottom: 0;
            }

            .sidebar .sidebar-title {
                font-size: 1.7rem;
                font-weight: bold;
                letter-spacing: 1px;
                color: #0dcaf0;
                background: #23272f;
            }

            .sidebar-nav {
                padding: 0;
                margin: 0;
                list-style: none;
            }

            .sidebar-nav li {
                margin-bottom: 2px;
            }

            .sidebar-nav a {
                color: #fff;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 14px;
                padding: 14px 28px;
                border-radius: 8px;
                font-size: 1.08rem;
                font-weight: 500;
                transition: background 0.15s, color 0.15s;
            }

            .sidebar-nav a.active,
            .sidebar-nav a:hover {
                background: #0dcaf0;
                color: #23272f;
            }

            .sidebar-nav a .fa-solid {
                width: 26px;
                text-align: center;
                font-size: 1.25rem;
            }

            @media (max-width: 992px) {
                .sidebar {
                    width: 60px;
                }

                .sidebar .sidebar-title {
                    font-size: 1.1rem;
                    padding: 12px 0;
                }

                .sidebar-nav a span {
                    display: none;
                }

                .sidebar-nav a {
                    justify-content: center;
                    padding: 14px 0;
                }
            }

            .header {
                background: #fff;
                border-bottom: 1px solid #dee2e6;
                min-height: 64px;
                box-shadow: 0 2px 8px #0001;
                margin-left: 240px;
                position: sticky;
                top: 0;
                z-index: 20;
            }

            .avatar {
                width: 38px;
                height: 38px;
                border-radius: 50%;
                object-fit: cover;
            }

            .main-content {
                padding: 36px 36px 24px 36px;
                min-height: 100vh;
                margin-left: 240px;
            }

            .main-box {
                background: #fff;
                border-radius: 14px;
                box-shadow: 0 2px 12px #0001;
                padding: 32px 24px;
            }

            .table thead th {
                vertical-align: middle;
            }

            .table-hover tbody tr:hover {
                background: #eaf6ff;
            }

            .filter-row .form-select,
            .filter-row .form-control {
                border-radius: 20px;
            }

            .modal-content {
                border-radius: 14px;
            }

            .modal-header,
            .modal-footer {
                border-color: #e9ecef;
            }

            .badge-status {
                font-size: 0.95em;
            }

            @media (max-width: 768px) {
                .main-box {
                    padding: 10px 2px;
                }

                .main-content {
                    padding: 10px 2px;
                }

                .table-responsive {
                    font-size: 0.95rem;
                }
            }
        </style>
    </head>

    <body>
        <div class="d-flex">
            <!-- Sidebar -->
            <nav class="sidebar p-0">
                <div class="sidebar-title text-center py-4 border-bottom border-secondary" style="cursor:pointer;"
                    onclick="location.href='index.jsp'">
                    <i class="fa-solid fa-people-group me-2"></i>ICSS
                </div>
                <ul class="sidebar-nav mt-3">
                    <li>
                        <a href="index.jsp"><i class="fa-solid fa-chart-line"></i><span>Dashboard</span></a>
                    </li>
                    <li>
                        <a href="./dsnhanvien"><i class="fa-solid fa-users"></i><span>Nhân sự</span></a>
                    </li>
                    <li>
                        <a href="task.jsp"><i class="fa-solid fa-tasks"></i><span>Công việc</span></a>
                    </li>
                    <li>
                        <a href="department.jsp"><i class="fa-solid fa-building"></i><span>Phòng ban</span></a>
                    </li>
                    <li>
                        <a href="attendance.jsp" class="active"><i class="fa-solid fa-calendar-check"></i><span>Chấm
                                công</span></a>
                    </li>
                    <li>
                        <a href="report.jsp"><i class="fa-solid fa-chart-bar"></i><span>Báo cáo</span></a>
                    </li>
                </ul>
            </nav>
            <!-- Main -->
            <div class="flex-grow-1">
                <!-- Header -->
                <%@ include file="header.jsp" %>
                    <div class="main-content">
                        <div class="main-box">
                            <!-- Thông báo -->
                            <% 
                            String successMsg = (String) session.getAttribute("successMsg");
                            String errorMsg = (String) session.getAttribute("errorMsg");
                            if (successMsg != null) {
                                session.removeAttribute("successMsg");
                            %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fa-solid fa-check-circle me-2"></i><%= successMsg %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% } %>
                            
                            <% if (errorMsg != null) {
                                session.removeAttribute("errorMsg");
                            %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fa-solid fa-exclamation-circle me-2"></i><%= errorMsg %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% } %>
                            
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h3 class="mb-0"><i class="fa-solid fa-calendar-check me-2"></i>Chấm công & Lương</h3>
                                <button class="btn btn-outline-success rounded-pill px-3" data-bs-toggle="modal"
                                    data-bs-target="#modalExportPayroll">
                                    <i class="fa-solid fa-file-export"></i> Xuất phiếu lương
                                </button>
                            </div>
                            <div class="row mb-3 filter-row g-2">
                                <div class="col-md-3">
                                    <input type="text" class="form-control" placeholder="Tìm kiếm theo tên, email..." id="searchInput">
                                </div>
                                <div class="col-md-2">
                                    <select class="form-select" name="thang">
                                        <option value="">Tháng</option>
                                        <% for(int i = 1; i <= 12; i++) { %>
                                            <option value="<%= i %>" <%= (i == thang) ? "selected" : "" %>><%= i %></option>
                                        <% } %>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <select class="form-select" name="nam">
                                        <option value="">Năm</option>
                                        <% for(int i = namHienTai - 2; i <= namHienTai + 1; i++) { %>
                                            <option value="<%= i %>" <%= (i == nam) ? "selected" : "" %>><%= i %></option>
                                        <% } %>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-outline-secondary w-100 rounded-pill" onclick="filterData()">
                                        <i class="fa-solid fa-filter"></i> Lọc
                                    </button>
                                </div>
                                <div class="col-md-3">
                                    <button class="btn btn-success rounded-pill px-3" data-bs-toggle="modal" data-bs-target="#modalChamCong">
                                        <i class="fa-solid fa-plus"></i> Chấm công
                                    </button>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-bordered align-middle table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>#</th>
                                            <th>Họ tên</th>
                                            <th>Phòng ban</th>
                                            <th>Ngày</th>
                                            <th>Check-in</th>
                                            <th>Check-out</th>
                                            <th>Số giờ</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        if(dsChamCong != null && !dsChamCong.isEmpty()) {
                                            int stt = 1;
                                            for(ChamCong cc : dsChamCong) {
                                        %>
                                        <tr>
                                            <td><%= stt++ %></td>
                                            <td>
                                                <span class="fw-semibold text-primary">
                                                    <%= cc.getHoTenNhanVien() %>
                                                </span>
                                            </td>
                                            <td><%= cc.getTenPhongBan() != null ? cc.getTenPhongBan() : "Chưa có" %></td>
                                            <td><%= sdf.format(cc.getNgay()) %></td>
                                            <td>
                                                <%= cc.getCheckIn() != null ? timeFormat.format(cc.getCheckIn()) : "-" %>
                                            </td>
                                            <td>
                                                <%= cc.getCheckOut() != null ? timeFormat.format(cc.getCheckOut()) : "-" %>
                                            </td>
                                            <td>
                                                <%= cc.getSoGioLam() %> giờ
                                            </td>
                                            <td>
                                                <% 
                                                String trangThai = cc.getTrangThaiChamCong();
                                                String badgeClass = "";
                                                if("Đầy đủ".equals(trangThai)) {
                                                    badgeClass = "bg-success";
                                                } else if("Chưa ra".equals(trangThai)) {
                                                    badgeClass = "bg-warning text-dark";
                                                } else {
                                                    badgeClass = "bg-danger";
                                                }
                                                %>
                                                <span class="badge <%= badgeClass %> badge-status"><%= trangThai %></span>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-info rounded-circle" 
                                                        data-bs-toggle="modal" data-bs-target="#modalDetailAttendance">
                                                    <i class="fa-solid fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <% 
                                            }
                                        } else {
                                        %>
                                        <tr>
                                            <td colspan="9" class="text-center py-4">
                                                <i class="fa-solid fa-calendar-times fa-3x text-muted mb-3"></i>
                                                <br>Không có dữ liệu chấm công tháng <%= thang %>/<%= nam %>
                                            </td>
                                        </tr>
                                        <% } %>
                                                    style="cursor:pointer;" data-bs-toggle="modal"
                                                    data-bs-target="#modalDetailAttendance">Lê Văn C</span>
                                            </td>
                                            <td>Nhân sự</td>
                                            <td>01/06/2024</td>
                                            <td>10/06/2024</td>
                                            <td>-</td>
                                            <td>-</td>
                                            <td>0</td>
                                            <td><span class="badge bg-danger badge-status">Vắng</span></td>
                                            <td>0đ</td>
                                            <td>
                                                <button class="btn btn-sm btn-info rounded-circle"
                                                    data-bs-toggle="modal" data-bs-target="#modalDetailAttendance"><i
                                                        class="fa-solid fa-eye"></i></button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- Modal chi tiết chấm công với tab -->
                        <div class="modal fade" id="modalDetailAttendance" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><i class="fa-solid fa-calendar-day"></i> Chi tiết chấm
                                            công</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <ul class="nav nav-tabs mb-3" id="attendanceDetailTab" role="tablist">
                                            <li class="nav-item" role="presentation">
                                                <button class="nav-link active" id="tab-att-info" data-bs-toggle="tab"
                                                    data-bs-target="#tabAttInfo" type="button" role="tab">Thông
                                                    tin</button>
                                            </li>
                                            <li class="nav-item" role="presentation">
                                                <button class="nav-link" id="tab-att-history" data-bs-toggle="tab"
                                                    data-bs-target="#tabAttHistory" type="button" role="tab">Lịch sử
                                                    chấm công</button>
                                            </li>
                                            <li class="nav-item" role="presentation">
                                                <button class="nav-link" id="tab-att-kpi" data-bs-toggle="tab"
                                                    data-bs-target="#tabAttKPI" type="button" role="tab">Lương &
                                                    KPI</button>
                                            </li>
                                        </ul>
                                        <div class="tab-content" id="attendanceDetailTabContent">
                                            <div class="tab-pane fade show active" id="tabAttInfo" role="tabpanel">
                                                <div class="row">
                                                    <div class="col-md-3 text-center">
                                                        <img src="https://i.pravatar.cc/100?img=1"
                                                            class="rounded-circle mb-2" width="80">
                                                        <div class="fw-bold">Nguyễn Văn A</div>
                                                        <div class="text-muted small">Kỹ thuật</div>
                                                    </div>
                                                    <div class="col-md-9">
                                                        <b>Ngày:</b> 10/06/2024<br>
                                                        <b>Check-in:</b> 08:00<br>
                                                        <b>Check-out:</b> 17:00<br>
                                                        <b>Số giờ:</b> 8<br>
                                                        <b>Trạng thái:</b> <span class="badge bg-success">Đủ
                                                            công</span><br>
                                                        <b>Lương ngày:</b> 350,000đ
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane fade" id="tabAttHistory" role="tabpanel">
                                                <ul id="attendanceHistoryList">
                                                    <li>09/06/2024: Đủ công</li>
                                                    <li>10/06/2024: Đi trễ</li>
                                                    <!-- AJAX load từ bảng cham_cong -->
                                                </ul>
                                            </div>
                                            <div class="tab-pane fade" id="tabAttKPI" role="tabpanel">
                                                <ul id="attendanceSalaryKPI">
                                                    <li>Lương tháng 6: 7,800,000đ</li>
                                                    <li>KPI: 8.5</li>
                                                    <!-- AJAX load từ bảng luong và luu_kpi -->
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Modal chấm công -->
                        <div class="modal fade" id="modalChamCong" tabindex="-1">
                            <div class="modal-dialog">
                                <form class="modal-content" method="post" action="attendance_handler.jsp">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><i class="fa-solid fa-clock"></i> Chấm công</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <input type="hidden" name="action" value="checkin">
                                        <div class="mb-3">
                                            <label class="form-label">Nhân viên</label>
                                            <select class="form-select" name="nhanVienId" required>
                                                <option value="">Chọn nhân viên</option>
                                                <% for(NhanVien nv : dsNhanVien) { %>
                                                    <option value="<%= nv.getId() %>"><%= nv.getHoTen() %> - <%= nv.getTenPhongBan() %></option>
                                                <% } %>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label">Ngày</label>
                                            <input type="date" class="form-control" name="ngay" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label">Loại chấm công</label>
                                            <select class="form-select" name="loai" required>
                                                <option value="checkin">Check-in</option>
                                                <option value="checkout">Check-out</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary rounded-pill">Chấm công</button>
                                        <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Huỷ</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                        <!-- Modal xuất phiếu lương -->
                        <div class="modal fade" id="modalExportPayroll" tabindex="-1">
                            <div class="modal-dialog">
                                <form class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title"><i class="fa-solid fa-file-export"></i> Xuất phiếu lương
                                        </h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label class="form-label">Chọn tháng</label>
                                            <select class="form-select" name="month">
                                                <!-- AJAX load tháng/năm -->
                                            </select>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary rounded-pill">Xuất file</button>
                                        <button type="button" class="btn btn-secondary rounded-pill"
                                            data-bs-dismiss="modal">Huỷ</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
            </div>
        </div>

        <script>
            function filterData() {
                const thang = document.querySelector('[name="thang"]').value;
                const nam = document.querySelector('[name="nam"]').value;
                
                let url = 'attendance.jsp?';
                if (thang) url += 'thang=' + thang + '&';
                if (nam) url += 'nam=' + nam;
                
                window.location.href = url;
            }
            
            // Set current date for chấm công
            document.addEventListener('DOMContentLoaded', function() {
                const today = new Date().toISOString().split('T')[0];
                const dateInput = document.querySelector('[name="ngay"]');
                if (dateInput) {
                    dateInput.value = today;
                }
            });
        </script>
    </body>
</html>