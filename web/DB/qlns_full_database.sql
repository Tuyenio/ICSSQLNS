-- Tạo Database
CREATE DATABASE IF NOT EXISTS qlns CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlns;

-- 1. Bảng phòng ban
CREATE TABLE phong_ban (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_phong VARCHAR(100) NOT NULL,
    truong_phong_id INT DEFAULT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng nhân viên
CREATE TABLE nhanvien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    so_dien_thoai VARCHAR(20),
    gioi_tinh ENUM('Nam', 'Nữ', 'Khác'),
    ngay_sinh DATE,
    phong_ban_id INT,
    chuc_vu VARCHAR(100),
    trang_thai_lam_viec ENUM('DangLam', 'TamNghi', 'NghiViec') DEFAULT 'DangLam',
    vai_tro ENUM('admin', 'quanly', 'nhanvien') DEFAULT 'nhanvien',
    ngay_vao_lam DATE,
    avatar_url VARCHAR(255),
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (phong_ban_id) REFERENCES phong_ban(id) ON DELETE SET NULL
);

-- Cập nhật khóa ngoại cho trưởng phòng (sau khi bảng nhân viên được tạo)
ALTER TABLE phong_ban ADD CONSTRAINT fk_truong_phong FOREIGN KEY (truong_phong_id) REFERENCES nhanvien(id) ON DELETE SET NULL;

-- 3. Bảng nhóm công việc
CREATE TABLE nhom_cong_viec (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_nhom VARCHAR(100),
    mo_ta TEXT,
    nguoi_tao_id INT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_tao_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 4. Thành viên nhóm
CREATE TABLE nhom_thanh_vien (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nhom_id INT,
    nhan_vien_id INT,
    vai_tro_nhom ENUM('ThanhVien', 'NhomTruong') DEFAULT 'ThanhVien',
    FOREIGN KEY (nhom_id) REFERENCES nhom_cong_viec(id) ON DELETE CASCADE,
    FOREIGN KEY (nhan_vien_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 5. Công việc
CREATE TABLE cong_viec (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_cong_viec VARCHAR(255) NOT NULL,
    mo_ta TEXT,
    han_hoan_thanh DATE,
    muc_do_uu_tien ENUM('Thap', 'TrungBinh', 'Cao') DEFAULT 'TrungBinh',
    nguoi_giao_id INT,
    nguoi_nhan_id INT,
    nhom_id INT,
    trang_thai ENUM('ChuaBatDau', 'DangThucHien', 'DaHoanThanh', 'TreHan') DEFAULT 'ChuaBatDau',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_giao_id) REFERENCES nhanvien(id) ON DELETE CASCADE,
    FOREIGN KEY (nguoi_nhan_id) REFERENCES nhanvien(id) ON DELETE CASCADE,
    FOREIGN KEY (nhom_id) REFERENCES nhom_cong_viec(id) ON DELETE CASCADE
);

-- 6. Theo dõi tiến độ công việc
CREATE TABLE cong_viec_tien_do (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cong_viec_id INT,
    nguoi_cap_nhat_id INT,
    phan_tram INT CHECK (phan_tram BETWEEN 0 AND 100),
    ghi_chu TEXT,
    file_dinh_kem VARCHAR(255),
    thoi_gian_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE,
    FOREIGN KEY (nguoi_cap_nhat_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 7. Lịch sử công việc
CREATE TABLE cong_viec_lich_su (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cong_viec_id INT,
    nguoi_thay_doi_id INT,
    mo_ta_thay_doi TEXT,
    thoi_gian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE,
    FOREIGN KEY (nguoi_thay_doi_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 8. Đánh giá công việc
CREATE TABLE cong_viec_danh_gia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cong_viec_id INT,
    nguoi_danh_gia_id INT,
    diem INT CHECK (diem BETWEEN 1 AND 10),
    nhan_xet TEXT,
    thoi_gian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE,
    FOREIGN KEY (nguoi_danh_gia_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 9. Chấm công
CREATE TABLE cham_cong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nhan_vien_id INT,
    ngay DATE,
    check_in TIME,
    check_out TIME,
    FOREIGN KEY (nhan_vien_id) REFERENCES nhanvien(id) ON DELETE CASCADE,
    UNIQUE(nhan_vien_id, ngay)
);

-- 10. Lương
CREATE TABLE luong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nhan_vien_id INT,
    thang INT,
    nam INT,
    tong_gio_lam FLOAT,
    luong_co_ban DECIMAL(12,2),
    thuong DECIMAL(12,2),
    phat DECIMAL(12,2),
    tong_luong DECIMAL(12,2),
    ngay_tinh DATE,
    FOREIGN KEY (nhan_vien_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 11. Thông báo
CREATE TABLE thong_bao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tieu_de VARCHAR(255),
    noi_dung TEXT,
    nguoi_nhan_id INT,
    da_doc BOOLEAN DEFAULT FALSE,
    loai_thong_bao ENUM('TaskMoi', 'Deadline', 'TreHan'),
    thoi_gian_gui TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_nhan_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 12. Báo cáo công việc
CREATE TABLE bao_cao_cong_viec (
    id INT PRIMARY KEY AUTO_INCREMENT,
    loai_bao_cao ENUM('PDF', 'Excel'),
    duong_dan VARCHAR(255),
    nguoi_tao_id INT,
    thoi_gian_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_tao_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);


-- 13. File đính kèm (của công việc hoặc tiến độ)
CREATE TABLE file_dinh_kem (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cong_viec_id INT,
    tien_do_id INT,
    duong_dan_file VARCHAR(255),
    mo_ta TEXT,
    thoi_gian_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE,
    FOREIGN KEY (tien_do_id) REFERENCES cong_viec_tien_do(id) ON DELETE CASCADE
);

-- 14. Cấu hình công thức lương
CREATE TABLE luong_cau_hinh (
    id INT PRIMARY KEY AUTO_INCREMENT,
    muc_luong_co_ban DECIMAL(12,2),
    luong_gio DECIMAL(12,2),
    he_so_kpi FLOAT DEFAULT 1.0,
    ngay_ap_dung DATE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 15. Ghi nhận KPI theo công việc
CREATE TABLE luu_kpi (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nhan_vien_id INT,
    cong_viec_id INT,
    diem_kpi FLOAT,
    thang INT,
    nam INT,
    FOREIGN KEY (nhan_vien_id) REFERENCES nhanvien(id) ON DELETE CASCADE,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE
);

-- 16. Lịch sử thay đổi nhân sự
CREATE TABLE nhan_su_lich_su (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nhan_vien_id INT,
    truong_thay_doi VARCHAR(100),
    gia_tri_cu TEXT,
    gia_tri_moi TEXT,
    thay_doi_bo_boi VARCHAR(100),
    thoi_gian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nhan_vien_id) REFERENCES nhanvien(id) ON DELETE CASCADE
);

-- 17. Phân quyền động cho chức năng
CREATE TABLE phan_quyen_chuc_nang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    vai_tro ENUM('admin', 'quanly', 'nhanvien'),
    chuc_nang VARCHAR(100),
    duoc_phep BOOLEAN DEFAULT FALSE
);

-- 18. Cấu hình hệ thống
CREATE TABLE cau_hinh_he_thong (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_cau_hinh VARCHAR(100) UNIQUE,
    gia_tri TEXT,
    mo_ta TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 19. Cấu hình quy trình công việc
CREATE TABLE cong_viec_quy_trinh (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cong_viec_id INT,
    ten_buoc VARCHAR(255),
    mo_ta TEXT,
    trang_thai ENUM('ChuaBatDau', 'DangLam', 'HoanThanh') DEFAULT 'ChuaBatDau',
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    thoi_gian_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cong_viec_id) REFERENCES cong_viec(id) ON DELETE CASCADE
);

-- Dữ liệu mẫu cho phòng ban (bổ sung thêm phòng ban)
INSERT INTO phong_ban (ten_phong, ngay_tao) VALUES
('Phòng Nhân sự', NOW()),
('Phòng Kỹ thuật', NOW()),
('Phòng Kế toán', NOW()),
('Phòng Kinh doanh', NOW());

-- Dữ liệu mẫu cho nhân viên (bổ sung trạng thái, vai trò, phòng ban khác nhau)
INSERT INTO nhanvien (ho_ten, email, mat_khau, so_dien_thoai, gioi_tinh, ngay_sinh, phong_ban_id, chuc_vu, trang_thai_lam_viec, vai_tro, ngay_vao_lam, avatar_url)
VALUES
('Nguyễn Văn A', 'a@company.com', '123456', '0900000001', 'Nam', '1990-01-01', 1, 'Trưởng phòng', 'DangLam', 'admin', '2020-01-01', NULL),
('Trần Thị B', 'b@company.com', '123456', '0900000002', 'Nữ', '1992-02-02', 2, 'Nhân viên', 'DangLam', 'nhanvien', '2021-02-01', NULL),
('Lê Văn C', 'c@company.com', '123456', '0900000003', 'Nam', '1995-03-03', 2, 'Quản lý', 'TamNghi', 'quanly', '2019-03-01', NULL),
('Phạm Thị D', 'd@company.com', '123456', '0900000004', 'Nữ', '1993-04-04', 3, 'Kế toán', 'NghiViec', 'nhanvien', '2022-04-01', NULL),
('Ngô Văn E', 'e@company.com', '123456', '0900000005', 'Nam', '1991-05-05', 4, 'Nhân viên', 'DangLam', 'nhanvien', '2023-05-01', NULL);

-- Cập nhật trưởng phòng cho phòng ban
UPDATE phong_ban SET truong_phong_id = 1 WHERE id = 1;
UPDATE phong_ban SET truong_phong_id = 3 WHERE id = 2;

-- Dữ liệu mẫu cho nhóm công việc
INSERT INTO nhom_cong_viec (ten_nhom, mo_ta, nguoi_tao_id)
VALUES
('Nhóm Dự án A', 'Nhóm thực hiện dự án A', 1),
('Nhóm Dự án B', 'Nhóm thực hiện dự án B', 3);

-- Dữ liệu mẫu cho thành viên nhóm
INSERT INTO nhom_thanh_vien (nhom_id, nhan_vien_id, vai_tro_nhom)
VALUES
(1, 1, 'NhomTruong'),
(1, 2, 'ThanhVien'),
(2, 3, 'NhomTruong'),
(2, 4, 'ThanhVien');
-- Dữ liệu mẫu cho công việc
INSERT INTO cong_viec (ten_cong_viec, mo_ta, han_hoan_thanh, muc_do_uu_tien, nguoi_giao_id, nguoi_nhan_id, nhom_id, trang_thai)
VALUES
('Thiết kế hệ thống', 'Thiết kế kiến trúc hệ thống', '2024-06-30', 'Cao', 1, 2, 1, 'DangThucHien'),
('Kiểm thử phần mềm', 'Kiểm thử chức năng phần mềm', '2024-07-15', 'TrungBinh', 3, 4, 2, 'ChuaBatDau'),
('Báo cáo tiến độ', 'Báo cáo tiến độ dự án', '2024-06-10', 'Cao', 1, 2, 1, 'TreHan'),
('Viết tài liệu', 'Viết tài liệu hướng dẫn', '2024-06-20', 'Thap', 3, 5, 2, 'DaHoanThanh');

-- Dữ liệu mẫu cho tiến độ công việc (đủ trạng thái)
INSERT INTO cong_viec_tien_do (cong_viec_id, nguoi_cap_nhat_id, phan_tram, ghi_chu)
VALUES
(1, 2, 50, 'Đã hoàn thành phần thiết kế cơ bản'),
(2, 4, 0, 'Chưa bắt đầu'),
(3, 2, 80, 'Đang hoàn thiện báo cáo'),
(4, 5, 100, 'Đã hoàn thành tài liệu');

-- Dữ liệu mẫu cho lương (có thưởng, phạt, trạng thái khác nhau)
INSERT INTO luong (nhan_vien_id, thang, nam, tong_gio_lam, luong_co_ban, thuong, phat, tong_luong, ngay_tinh)
VALUES
(1, 6, 2024, 160, 15000000, 2000000, 0, 17000000, CURDATE()),
(2, 6, 2024, 158, 12000000, 1000000, 500000, 12500000, CURDATE()),
(3, 6, 2024, 140, 13000000, 0, 1000000, 12000000, CURDATE()),
(4, 6, 2024, 0, 11000000, 0, 0, 0, CURDATE()),
(5, 6, 2024, 155, 11500000, 500000, 0, 12000000, CURDATE());

-- Dữ liệu mẫu cho thông báo (các loại, trạng thái đọc/chưa đọc)
INSERT INTO thong_bao (tieu_de, noi_dung, nguoi_nhan_id, loai_thong_bao, da_doc)
VALUES
('Công việc mới', 'Bạn có công việc mới được giao', 2, 'TaskMoi', FALSE),
('Sắp đến hạn', 'Công việc của bạn sắp đến hạn', 4, 'Deadline', FALSE),
('Công việc trễ hạn', 'Bạn có công việc bị trễ hạn', 2, 'TreHan', TRUE),
('Lương tháng 6', 'Lương tháng này đã được chuyển khoản', 1, 'TaskMoi', TRUE);

-- Dữ liệu mẫu cho đánh giá công việc (đa dạng điểm số, nhận xét)
INSERT INTO cong_viec_danh_gia (cong_viec_id, nguoi_danh_gia_id, diem, nhan_xet)
VALUES
(1, 1, 8, 'Hoàn thành tốt'),
(2, 3, 7, 'Cần cải thiện tiến độ'),
(3, 1, 5, 'Trễ hạn, cần rút kinh nghiệm'),
(4, 3, 9, 'Hoàn thành xuất sắc');

-- Dữ liệu mẫu cho KPI (đa dạng điểm số)
INSERT INTO luu_kpi (nhan_vien_id, cong_viec_id, diem_kpi, thang, nam)
VALUES
(2, 1, 8.5, 6, 2024),
(4, 2, 7.0, 6, 2024),
(5, 4, 9.0, 6, 2024);

-- Dữ liệu mẫu cho lịch sử thay đổi nhân sự (đa dạng trường hợp)
INSERT INTO nhan_su_lich_su (nhan_vien_id, truong_thay_doi, gia_tri_cu, gia_tri_moi, thay_doi_bo_boi)
VALUES
(2, 'Chức vụ', 'Nhân viên', 'Quản lý', 'admin'),
(4, 'Phòng ban', 'Kế toán', 'Kỹ thuật', 'quanly'),
(3, 'Trạng thái', 'Đang làm', 'Tạm nghỉ', 'admin');

-- Dữ liệu mẫu cho phân quyền chức năng (đủ các vai trò, chức năng)
INSERT INTO phan_quyen_chuc_nang (vai_tro, chuc_nang, duoc_phep)
VALUES
('admin', 'TaoCongViec', TRUE),
('admin', 'XoaNhanVien', TRUE),
('quanly', 'XemBaoCao', TRUE),
('quanly', 'TaoCongViec', TRUE),
('nhanvien', 'CapNhatTienDo', TRUE),
('nhanvien', 'XemLuong', TRUE);