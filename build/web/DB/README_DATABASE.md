# Hệ Thống Quản Lý Nhân Sự - Cơ Sở Dữ Liệu

## Tổng Quan
Hệ thống QLNS sử dụng MySQL với 19 bảng chính quản lý toàn bộ quy trình nhân sự từ tuyển dụng đến nghỉ việc.

## Cấu Trúc Database

### Bảng Chính (Core Tables)
- `phong_ban` - Quản lý các phòng ban
- `nhanvien` - Thông tin nhân viên
- `nhom_cong_viec` - Nhóm dự án
- `cong_viec` - Công việc/Task
- `cham_cong` - Chấm công hàng ngày
- `luong` - Bảng lương chi tiết
- `thong_bao` - Hệ thống thông báo

### Bảng Hỗ Trợ (Support Tables)
- `cong_viec_tien_do` - Tiến độ công việc
- `cong_viec_lich_su` - Lịch sử thay đổi công việc
- `cong_viec_danh_gia` - Đánh giá công việc
- `file_dinh_kem` - File đính kèm
- `bao_cao_cong_viec` - Báo cáo
- `nhan_su_lich_su` - Lịch sử thay đổi nhân sự
- `luu_kpi` - KPI nhân viên
- `phan_quyen_chuc_nang` - Phân quyền
- `cau_hinh_he_thong` - Cấu hình system
- `luong_cau_hinh` - Cấu hình lương
- `cong_viec_quy_trinh` - Quy trình công việc
- `nhom_thanh_vien` - Thành viên nhóm

## Dữ Liệu Mẫu

### Thống Kê Dữ Liệu
- **6 Phòng ban**: Kỹ thuật, Nhân sự, Marketing, Kế toán, Kinh doanh, Quản lý
- **15 Nhân viên**: Từ Giám đốc đến nhân viên mới, đầy đủ hierarchy
- **6 Nhóm dự án**: Website, QLNS, Marketing, Tuyển dụng, Báo cáo, Sự kiện
- **22 Công việc**: Đa dạng trạng thái và độ ưu tiên
- **330+ Records chấm công**: 3 tháng dữ liệu (6-8/2024)
- **30 Records lương**: Chi tiết các khoản thu/chi
- **10 Thông báo**: Các loại notification
- **100+ Records khác**: KPI, lịch sử, đánh giá...

### Dữ Liệu Nhân Viên Mẫu
1. **Lê Văn Minh** - Giám đốc (Lương: 15M)
2. **Trần Thị Lan** - Trưởng phòng NS (Lương: 12M)  
3. **Nguyễn Văn Cường** - Senior Developer (Lương: 10M)
4. **Phạm Thị Hoa** - Kế toán (Lương: 8M)
5. **Hoàng Minh Tuấn** - Nhân viên KD (Lương: 7M)
6. **Võ Thị Mai** - UI/UX Designer (Lương: 9M)
7. **Đặng Văn Nam** - Junior Developer (Lương: 6.5M)
8. **Lý Thị Hương** - Marketing Executive (Lương: 8.5M)
9. **Bùi Văn Đức** - Senior Accountant (Lương: 11M)
10. **Ngô Thị Linh** - HR Assistant (Lương: 7.5M)

### Tình Huống Thực Tế
- **Công việc trễ hạn**: Module chấm công đang trễ 
- **Thăng chức**: Nguyễn Văn Cường lên Senior
- **Tăng lương**: 4 nhân viên được tăng lương
- **KPI**: Đánh giá hiệu suất theo tháng
- **Chấm công**: Realistic data với đi muộn, về sớm
- **Thông báo**: Đa dạng loại tin nhắn

## Hướng Dẫn Import

### Cách 1: Import Trực Tiếp
```sql
-- Kết nối MySQL
mysql -u root -p

-- Tạo database
CREATE DATABASE qlns CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlns;

-- Import toàn bộ
SOURCE qlns_full_database.sql;
```

### Cách 2: Sử dụng Script Import
```sql
-- Chạy script tự động
SOURCE import_sample_data.sql;
```

### Cách 3: MySQL Workbench
1. Mở MySQL Workbench
2. File > Run SQL Script
3. Chọn file `qlns_full_database.sql`
4. Execute

## Kiểm Tra Dữ Liệu
```sql
-- Kiểm tra số lượng records
SELECT 
    (SELECT COUNT(*) FROM phong_ban) as phong_ban,
    (SELECT COUNT(*) FROM nhanvien) as nhan_vien,
    (SELECT COUNT(*) FROM cong_viec) as cong_viec,
    (SELECT COUNT(*) FROM cham_cong) as cham_cong,
    (SELECT COUNT(*) FROM luong) as luong;

-- Xem thông tin nhân viên
SELECT nv.ho_ten, pb.ten_phong_ban, nv.chuc_vu, nv.luong_co_ban
FROM nhanvien nv 
JOIN phong_ban pb ON nv.phong_ban_id = pb.id
ORDER BY nv.luong_co_ban DESC;

-- Xem tiến độ công việc
SELECT cv.ten_cong_viec, cv.trang_thai, 
       nv1.ho_ten as nguoi_giao, nv2.ho_ten as nguoi_nhan
FROM cong_viec cv
LEFT JOIN nhanvien nv1 ON cv.nguoi_giao_id = nv1.id  
LEFT JOIN nhanvien nv2 ON cv.nguoi_nhan_id = nv2.id;
```

## Cấu Hình Ứng Dụng

### Database Connection (Java)
```java
String url = "jdbc:mysql://localhost:3306/qlns";
String username = "root";
String password = "your_password";
```

### Tài Khoản Test
- **Admin**: admin/admin123
- **Manager**: manager/manager123  
- **Employee**: user/user123

## Maintenance

### Backup Database
```bash
mysqldump -u root -p qlns > qlns_backup_$(date +%Y%m%d).sql
```

### Reset Data
```sql
SOURCE import_sample_data.sql  -- Tự động xóa và import lại
```

### Update Sample Data
Chỉnh sửa file `qlns_full_database.sql` và import lại.

## Ghi Chú Kỹ Thuật
- Character Set: UTF-8 (utf8mb4)
- Engine: InnoDB với Foreign Keys
- Timezone: Asia/Ho_Chi_Minh
- Date Format: YYYY-MM-DD
- Numeric Format: VND không dấu phẩy

## Troubleshooting
1. **Lỗi charset**: Đảm bảo MySQL hỗ trợ utf8mb4
2. **Lỗi foreign key**: Import theo đúng thứ tự bảng
3. **Lỗi data**: Kiểm tra format ngày tháng và số
4. **Performance**: Index đã được tối ưu cho các query thường dùng

---
*Cập nhật cuối: December 2024*
*Version: 1.0 - Production Ready*
