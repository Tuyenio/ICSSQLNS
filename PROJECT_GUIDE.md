# ICSSQLNS - Hệ thống Quản lý Nhân sự

## Tổng quan Dự án
Hệ thống quản lý nhân sự (HR Management System) được xây dựng bằng Java Servlet/JSP với cơ sở dữ liệu MySQL.

### Công nghệ sử dụng
- **Backend**: Java Servlet/JSP
- **Database**: MySQL 
- **Server**: Apache Tomcat
- **IDE**: NetBeans
- **Architecture**: MVC Pattern với DAO

## Cấu trúc Dự án

### Backend Structure
```
src/java/
├── controller/          # Controllers xử lý logic nghiệp vụ
│   ├── CongViecController.java     # Quản lý công việc  
│   ├── LuongController.java        # Quản lý lương
│   ├── NhanVienController.java     # Quản lý nhân viên
│   ├── PhongBanController.java     # Quản lý phòng ban
│   └── ThongBaoController.java     # Quản lý thông báo
├── dao/                 # Data Access Objects
│   ├── ChamCongDAO.java            # Chấm công
│   ├── CongViecDAO.java            # Công việc
│   ├── LuongDAO.java               # Lương
│   ├── NhanVienDAO.java            # Nhân viên
│   ├── PhongBanDAO.java            # Phòng ban
│   └── ThongBaoDAO.java            # Thông báo
├── model/               # Entity models
├── servlet/             # Main servlet router
│   └── MainServlet.java
└── util/                # Utility classes
    ├── AuthUtil.java               # Xác thực
    ├── DateUtil.java               # Xử lý ngày tháng
    └── ValidationUtil.java         # Validation
```

### Database Schema
- **Nhân viên (NhanVien)**: Thông tin cá nhân, chức vụ, phòng ban
- **Phòng ban (PhongBan)**: Thông tin phòng ban, trưởng phòng
- **Công việc (CongViec)**: Task management, assignment, tracking
- **Chấm công (ChamCong)**: Attendance tracking
- **Lương (Luong)**: Salary calculation, approval workflow
- **Thông báo (ThongBao)**: Notification system

## Hướng dẫn Cài đặt

### 1. Cài đặt NetBeans
1. Tải và cài đặt NetBeans IDE
2. Cài đặt Apache Tomcat server
3. Cấu hình Tomcat trong NetBeans

### 2. Cài đặt Database
1. Cài đặt MySQL Server
2. Tạo database từ file `web/DB/qlns_full_database.sql`
3. Import sample data từ `web/DB/import_sample_data.sql`

### 3. Cấu hình Project
1. Mở project trong NetBeans
2. **QUAN TRỌNG**: Thêm Servlet API library:
   - Right-click project → Properties → Libraries
   - Click "Add Library" → Select "Servlet API" → Add Library
3. Cấu hình database connection trong DAO classes
4. Clean and Build project
5. Deploy to Tomcat server

### 4. Chạy ứng dụng
1. Start Tomcat server
2. Deploy project
3. Truy cập: `http://localhost:8080/ICSSQLNS`

## Chức năng Chính

### 1. Quản lý Nhân viên
- CRUD operations cho nhân viên
- Phân quyền theo vai trò (Admin/User)
- Profile management

### 2. Quản lý Công việc  
- Tạo, phân công task
- Theo dõi tiến độ
- Cập nhật trạng thái
- Notification system

### 3. Quản lý Lương
- Tính lương tự động
- Workflow phê duyệt
- Báo cáo thống kê
- Export data

### 4. Chấm công
- Check-in/Check-out
- Theo dõi giờ làm việc
- Báo cáo attendance

### 5. Thông báo
- System notifications
- Task assignments
- Status updates

## API Endpoints

### Authentication
- `POST /login` - User login
- `POST /logout` - User logout
- `POST /change-password` - Change password

### Employee Management  
- `GET /employee/list` - List employees
- `POST /employee/create` - Create employee
- `PUT /employee/update` - Update employee
- `DELETE /employee/delete` - Delete employee

### Task Management
- `GET /task/list` - List tasks
- `POST /task/create` - Create task
- `PUT /task/assign` - Assign task
- `PUT /task/update-status` - Update status

### Salary Management
- `GET /salary/list` - List salaries
- `POST /salary/calculate` - Calculate salary
- `PUT /salary/approve` - Approve salary
- `GET /salary/statistics` - Salary stats

## Troubleshooting

### Common Issues

1. **Servlet API not found**
   - Solution: Add Servlet API library in NetBeans project properties

2. **Database connection failed**
   - Check MySQL service status
   - Verify connection parameters in DAO classes
   - Ensure database exists and accessible

3. **Compilation errors**
   - Clean and rebuild project
   - Check all required libraries are added
   - Verify import statements

4. **Deployment issues**
   - Ensure Tomcat is running
   - Check web.xml configuration
   - Verify context path settings

### Error Resolution
- All compilation errors have been resolved
- Unused imports removed
- Duplicate class definitions fixed
- Servlet dependencies properly configured

## Development Notes

### Architecture Decisions
- Servlet-free controllers for framework independence
- DAO pattern for database abstraction
- Utility classes for common operations
- MVC separation of concerns

### Code Quality
- All major compilation errors resolved
- Unused imports cleaned up
- Warning suppressions where appropriate
- Consistent coding standards

### Security Considerations
- Authentication middleware
- Input validation
- SQL injection prevention
- Session management

## Database Information
Refer to `web/DB/README_DATABASE.md` for detailed database schema and setup instructions.

---
**Status**: Backend implementation complete - Ready for deployment after Servlet API library configuration
**Last Updated**: $(Get-Date -Format "yyyy-MM-dd HH:mm")
