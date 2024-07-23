-- Tạo database Qản lý học viên
CREATE DATABASE QLHocVien
GO

USE QLHocVien
GO

CREATE TABLE NhanVien (
    MaNV nvarchar(50) NOT NULL PRIMARY KEY,
    MatKhau nvarchar(50) NOT NULL,
    HoTen nvarchar(50) NOT NULL,
    VaiTro bit NOT NULL DEFAULT 0
);

CREATE TABLE ChuyenDe (
    MaCD nchar(5) NOT NULL PRIMARY KEY,
    TenCD nvarchar(50) NOT NULL,
    HocPhi float NOT NULL DEFAULT 0,
    ThoiLuong int NOT NULL DEFAULT 30,
    Hinh nvarchar(50) NOT NULL DEFAULT 'chuyen-de.png',
    MoTa nvarchar(255) NOT NULL,
    UNIQUE (TenCD),
    CHECK (HocPhi >= 0 AND ThoiLuong > 0)
);
GO

CREATE TABLE NguoiHoc (
    MaNH nchar(7) NOT NULL PRIMARY KEY,
    HoTen nvarchar(50) NOT NULL,
    NgaySinh date NOT NULL,
    GioiTinh bit NOT NULL DEFAULT 0,
    DienThoai nvarchar(50) NOT NULL,
    Email nvarchar(50) NOT NULL,
    GhiChu nvarchar(max) NULL,
    MaNV nvarchar(50) NOT NULL,
    NgayDK date NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON UPDATE CASCADE
);
GO

CREATE TABLE KhoaHoc (
    MaKH int IDENTITY(1,1) NOT NULL PRIMARY KEY,
    MaCD nchar(5) NOT NULL,
    HocPhi float NOT NULL DEFAULT 0,
    ThoiLuong int NOT NULL DEFAULT 0,
    NgayKG date NOT NULL,
    GhiChu nvarchar(50) NULL,
    MaNV nvarchar(50) NOT NULL,
    NgayTao date NOT NULL DEFAULT GETDATE(),
    CHECK (HocPhi >= 0 AND ThoiLuong > 0),
    FOREIGN KEY (MaCD) REFERENCES ChuyenDe(MaCD) ON UPDATE CASCADE,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON UPDATE CASCADE
);
GO

CREATE TABLE HocVien (
    MaHV int IDENTITY(1,1) NOT NULL PRIMARY KEY,
    MaKH int NOT NULL,
    MaNH nchar(7) NOT NULL,
    Diem float NOT NULL,
    UNIQUE (MaKH, MaNH),
    FOREIGN KEY (MaKH) REFERENCES KhoaHoc(MaKH) ON DELETE CASCADE,
    FOREIGN KEY (MaNH) REFERENCES NguoiHoc(MaNH) ON UPDATE CASCADE
);
GO




--Tạo các PROC
USE QLHocVien
GO

CREATE PROC sp_BangDiem(@MaKH INT)
AS BEGIN
	SELECT 
		nh.MaNH,
		nh.HoTen,
		hv.Diem
	FROM HocVien hv JOIN NguoiHoc nh ON nh.MaNH = hv.MaNH
	WHERE hv.MaKH = @MaKH
	ORDER BY hv.Diem DESC
END

GO

CREATE PROC sp_DiemChuyenDe
AS BEGIN
	SELECT
		TenCD ChuyenDe,
		COUNT(MaHV) SoHV,
		MIN(Diem) ThapNhat,
		MAX(Diem) CaoNhat,
		AVG(Diem) TrungBinh
	FROM KhoaHoc kh
	JOIN HocVien hv ON kh.MaKH = hv.MaKH
	JOIN ChuyenDe cd ON cd.MaCD = kh.MaCD
	GROUP BY TenCD
END

GO

CREATE PROC sp_DoanhThu(@Year INT)
AS BEGIN
	SELECT
		TenCD ChuyenDe,
		COUNT(DISTINCT kh.MaKH) SoKH,
		COUNT(hv.MaHV) SoHV,
		SUM(kh.HocPhi) DoanhThu,
		MIN(kh.HocPhi) ThapNhat,
		MAX(kh.HocPhi) CaoNhat,
		AVG(Kh.HocPhi) TrungBinh
	FROM KhoaHoc kh
	JOIN HocVien hv ON kh.MaKH = hv.MaKH
	JOIN ChuyenDe cd ON cd.MaCD = kh.MaCD
	WHERE YEAR(NgayKG) = @Year
	GROUP BY TenCD
END

GO

CREATE PROC sp_LuongNguoiHoc
AS BEGIN
	SELECT
		YEAR(NgayDK) Nam,
		COUNT(*) SoLuong,
		MIN(NgayDK) DauTien,
		MAX(NgayDK) CuoiCung
	FROM NguoiHoc
	GROUP BY YEAR(NgayDK)
END

GO




-- Thêm dữ liệu vào bảng NhanVien
INSERT INTO NhanVien VALUES
(N'thuynt', N'123456', N'Nguyễn Thị Thủy', 0),
(N'namvp', N'654321', N'Phạm Văn Nam', 0),
(N'mainh', N'password', N'Nguyễn Anh Mai', 1),
(N'thinhnc', N'thinh123', N'Nguyễn Công Thịnh', 0),
(N'linhnt', N'linh123', N'Nguyễn Thị Linh', 1);

-- Thêm dữ liệu vào bảng ChuyenDe
INSERT INTO ChuyenDe VALUES
(N'JAV01', N'Lập trình Java cơ bản', 2500, 90, N'GAME.png', N'JAV01 - Lập trình Java cơ bản'),
(N'JAV02', N'Lập trình Java nâng cao', 3000, 90, N'JSPR.png', N'JAV02 - Lập trình Java nâng cao'),
(N'WEB01', N'Thiết kế web với HTML và CSS', 2000, 70, N'WEBU.jpg', N'WEB01 - Thiết kế web với HTML và CSS'),
(N'PRO01', N'Dự án với Swing & JDBC', 3000, 90, N'VBPR.png', N'PRO01 - Dự án với Swing & JDBC');

-- Thêm dữ liệu vào bảng NguoiHoc
INSERT INTO NguoiHoc VALUES
(N'PY00060', N'Vũ Thị Hương', '2005-10-12', 0, N'0388928274', N'huongvt@fpt.edu.vn', N'0388928274 - Vũ Thị Hương', N'namvp', '2024-02-20'),
(N'PY00061', N'Trần Đức Anh', '2005-08-15', 1, N'0388928197', N'anhtd@fpt.edu.vn', N'0388928197 - Trần Đức Anh', N'mainh', '2024-03-05'),
(N'PY00062', N'Huỳnh Minh Tuấn', '2005-11-25', 1,  N'0388928311', N'tuanhm@fpt.edu.vn', N'0388928311 - Huỳnh Minh Tuấn', N'thuynt', '2024-04-10'),
(N'PY00063', N'Phạm Thị Hằng', '2005-09-01', 0, N'0388928357', N'hangpt@fpt.edu.vn', N'0388928357 - Phạm Thị Hằng', N'thinhnc', '2024-06-10'),
(N'PY00064', N'Trần Văn Khải', '2005-07-12', 1, N'0388928496', N'khaitv@fpt.edu.vn', N'0388928496 - Trần Văn Khải', N'thuynt', '2024-06-15'),
(N'PY00065', N'Hoàng Thị Ái', '2005-12-20', 0, N'0388928224', N'aiht@fpt.edu.vn', N'0388928224 - Hoàng Thị Ái', N'namvp', '2024-06-20'),
(N'PY00031', N'Đỗ Ngọc Linh', '2005-04-12', 0, N'0388928437', N'linhdnpy00031@fpt.edu.vn', N'0388928437 - Đỗ Ngọc Linh', N'thuynt', '2024-06-05'),
(N'PY00055', N'Nguyễn Thị Sáu','2005-12-05', 0, N'0388928867', N'sauntpy00055@fpt.edu.vn', N'0388928867 - Nguyễn Thị Sáu', N'thinhnc','2024-11-20'),
(N'PY00025', N'Huỳnh Phúc Khang','2005-09-15' , 1, N'0388928246', N'khanghppy00025@fpt.edu.vn', N'0388928246 - Huỳnh Phúc Khang', N'mainh','2024-03-13'),
(N'PY00028', N'Nguyễn Thuỳ Linh','2005-07-17', 0, N'0388928368', N'linhntpy00028@fpt.edu.vn', N'0388928368 - Nguyễn Thuỳ Linh', N'namvp','2024-01-16'),
(N'PY00056', N'Tô Lâm', '2005-09-21', 1, N'0388928689', N'lamtpy00056@fpt.edu.vn', N'0388928689 - Tô Lâm', N'linhnt','2024-10-19');

-- Thêm dữ liệu vào bảng KhoaHoc
INSERT INTO KhoaHoc VALUES
(N'JAV01', 2500, 90, '2024-09-05', N'Lập trình Java cơ bản', N'namvp', '2024-09-06'),
(N'JAV02', 3000, 90, '2024-11-06', N'Lập trình Java nâng cao', N'mainh', '2024-11-16'),
(N'WEB01', 2000, 70, '2024-10-12', N'Thiết kế web với HTML và CSS', N'thuynt', '2024-10-13'),
(N'PRO01', 3000, 90, '2024-12-05', N'Lập trình Java cơ bản', N'namvp', '2024-12-06'),
(N'JAV01', 2500, 90, '2024-08-10', N'Lập trình Java cơ bản', N'thinhnc', '2024-08-11'),
(N'JAV02', 3000, 90, '2024-10-05', N'Lập trình Java nâng cao', N'linhnt', '2024-10-06'),
(N'WEB01', 2000, 70, '2024-11-05', N'Thiết kế web với HTML và CSS', N'linhnt', '2024-11-06'),
(N'JAV01', 2500, 90, '2024-11-11', N'Lập trình Java cơ bản', N'thuynt', '2024-11-16');

-- Thêm dữ liệu vào bảng HocVien
INSERT INTO HocVien VALUES
(1, N'PY00060', 8),
(2, N'PY00061', 8),
(3, N'PY00062', 9),
(4, N'PY00063', 6),
(5, N'PY00064', 7),
(6, N'PY00065', 5),
(8, N'PY00031', 8),
(1, N'PY00055', 9),
(2, N'PY00025', 5),
(3, N'PY00028', 9),
(4, N'PY00056', 8);

