
DROP DATABASE IF EXISTS `quan_cafe`;
CREATE DATABASE `quan_cafe`;
USE `quan_cafe`;

CREATE TABLE `ban` (
	`Maban` INT(11) NOT NULL DEFAULT '0',
	`Trangthai` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`Maban`) USING BTREE
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `nhanvien` (
	`Ma_NV` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ten` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`CCCD` VARCHAR(12) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`SDT` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Birth` DATE NULL DEFAULT NULL,
	`Gioitinh` TINYINT(2) NULL DEFAULT NULL,
	`Role` TINYINT(2) NULL DEFAULT NULL,
	`Luong` DOUBLE NULL DEFAULT NULL,
	
	`user` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`pass` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`Ma_NV`) USING BTREE
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `vattu` (
	`Ma_Vattu` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ten_vattu` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Soluong_vattu` INT(11) NULL DEFAULT NULL,
	`Gia` DOUBLE NULL DEFAULT NULL,
	`sl_hong` INT(11) NULL DEFAULT NULL,
	`Ngay_Nhap` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Vattu`) USING BTREE
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `nguyenlieu` (
	`Ma_Nglieu` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ten_Nglieu` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Soluong_NL` INT(11) NULL DEFAULT NULL,
	`Gia` DOUBLE NULL DEFAULT NULL,
	`Soluong_NLin` INT(11) NULL DEFAULT NULL,
	`Date_Nglieu` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Nglieu`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `loaimon` (
	`Ma_LM` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ten_LM` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`Ma_LM`) USING BTREE
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `mon` (
	`Ma_Mon` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_general_ci',
	`Ten_mon` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Gia_mon` DOUBLE NULL DEFAULT NULL,
	`Ma_LM` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Hinhanh` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`Ma_Mon`) USING BTREE,
	INDEX `FK_món_loại món` (`Ma_LM`) USING BTREE,
	CONSTRAINT `FK_món_loại món` FOREIGN KEY (`Ma_LM`) REFERENCES `loaimon` (`Ma_LM`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `bill` (
	`Ma_Bill` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ma_NV` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`tonggia` DOUBLE NULL DEFAULT '0',
	`ban` INT(11) NOT NULL,
	`date` DATETIME NULL DEFAULT NULL,
	`Trangthai` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`Ma_Bill`) USING BTREE,
	INDEX `FK_bill_nhân viên` (`Ma_NV`) USING BTREE,
	INDEX `FK_bill_ban` (`ban`) USING BTREE,
	CONSTRAINT `FK_bill_ban` FOREIGN KEY (`ban`) REFERENCES `ban` (`Maban`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_bill_nhân viên` FOREIGN KEY (`Ma_NV`) REFERENCES `nhanvien` (`Ma_NV`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `order` (
	`Ma_Bill` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ma_Mon` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`soluong` INT(11) NULL DEFAULT NULL,
	`Gia` DOUBLE NULL DEFAULT NULL,
	`Thanhtien` DOUBLE NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Bill`, `Ma_Mon`) USING BTREE,
	INDEX `FK_order_món` (`Ma_Mon`) USING BTREE,
	CONSTRAINT `FK_order_bill` FOREIGN KEY (`Ma_Bill`) REFERENCES `bill` (`Ma_Bill`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_order_món` FOREIGN KEY (`Ma_Mon`) REFERENCES `mon` (`Ma_Mon`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `nhapnguyenlieu` (
	`Ma_Nhap` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ma_Nguyenlieu` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Gia` DOUBLE NULL DEFAULT NULL,
	`TongTien` DOUBLE NULL DEFAULT NULL,
	`Ngaynhap` DATE NULL DEFAULT NULL,
	`Soluong` DOUBLE NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Nhap`) USING BTREE,
	INDEX `FK_nhapnguyenlieu_nguyenlieu` (`Ma_Nguyenlieu`) USING BTREE,
	CONSTRAINT `FK_nhapnguyenlieu_nguyenlieu` FOREIGN KEY (`Ma_Nguyenlieu`) REFERENCES `nguyenlieu` (`Ma_Nglieu`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `haohut` (
	`Ma_Hao` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ma_Vattu` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_general_ci',
	`Tenvattu` VARCHAR(50) NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Gia_vattu` DOUBLE NULL DEFAULT '0',
	`Date_Nhap` DATE NULL DEFAULT NULL,
	`Tonggia_VT` DOUBLE NULL DEFAULT '0',
	`SL_VT` INT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Hao`) USING BTREE,
	INDEX `FK_hao hụt_vật tư` (`Ma_Vattu`) USING BTREE,
	CONSTRAINT `FK_hao hụt_vật tư` FOREIGN KEY (`Ma_Vattu`) REFERENCES `vattu` (`Ma_Vattu`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

CREATE TABLE `chebien` (
	`Ma_Mon` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Ma_Nglieu` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
	`Soluong_chebien` DOUBLE NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_Mon`, `Ma_Nglieu`) USING BTREE,
	INDEX `FK_chế biến_nguyên liệu` (`Ma_Nglieu`) USING BTREE,
	CONSTRAINT `FK_chế biến_món` FOREIGN KEY (`Ma_Mon`) REFERENCES `mon` (`Ma_Mon`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_chế biến_nguyên liệu` FOREIGN KEY (`Ma_Nglieu`) REFERENCES `nguyenlieu` (`Ma_Nglieu`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `ca` (
	`Ma_ca` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ngay` DATE NOT NULL,
	`Time_start` DATETIME NULL DEFAULT NULL,
	`Time_end` DATETIME NULL DEFAULT NULL,
	`Luong_Ca` DOUBLE NULL DEFAULT NULL,
	`Soluong_ca` INT(11) NULL DEFAULT NULL,
	`Soluong_dky` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_ca`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
CREATE TABLE `chia_lich_lam_viec` (
	`Ma_phanlich` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ma_Nhanvien` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Ngay` DATE NULL DEFAULT NULL,
	`Ca` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`Luong` DOUBLE NULL DEFAULT NULL,
	`Trang_thai` TINYINT(4) NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_phanlich`) USING BTREE,
	INDEX `FK_chia_lich_lam_viec_nhanvien` (`Ma_Nhanvien`) USING BTREE,
	INDEX `FK_chia_lich_lam_viec_ca` (`Ca`) USING BTREE,
	CONSTRAINT `FK_chia_lich_lam_viec_ca` FOREIGN KEY (`Ca`) REFERENCES `ca` (`Ma_ca`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_chia_lich_lam_viec_nhanvien` FOREIGN KEY (`Ma_Nhanvien`) REFERENCES `nhanvien` (`Ma_NV`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
CREATE TABLE `doi_ca` (
	`Ma_doica` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ma_Phanlich1` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ma_Phanlich2` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ma_NV1` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Ma_NV2` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`Trang_thai` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`Ma_doica`) USING BTREE,
	INDEX `IDX_Phanlich1` (`Ma_Phanlich1`) USING BTREE,
	INDEX `IDX_Phanlich2` (`Ma_Phanlich2`) USING BTREE,
	INDEX `IDX_NV1` (`Ma_NV1`) USING BTREE,
	INDEX `IDX_NV2` (`Ma_NV2`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

SQLSEVER
-- Database creation
IF DB_ID('quan_cafe') IS NOT NULL
    DROP DATABASE quan_cafe;
GO

CREATE DATABASE quan_cafe;
GO

USE quan_cafe;
GO

-- Table: ban
CREATE TABLE ban (
    Maban INT NOT NULL DEFAULT 0,
    Trangthai NVARCHAR(50) NULL,
    CONSTRAINT PK_ban PRIMARY KEY (Maban)
);

-- Table: nhanvien
CREATE TABLE nhanvien (
    Ma_NV NVARCHAR(50) NOT NULL,
    Ten NVARCHAR(50),
    CCCD NVARCHAR(12),
    SDT NVARCHAR(50),
    Birth DATE,
    Gioitinh TINYINT,
    Role TINYINT,
    Luong FLOAT,
    [user] NVARCHAR(50),
    [pass] NVARCHAR(50),
    CONSTRAINT PK_nhanvien PRIMARY KEY (Ma_NV)
);

-- Table: vattu
CREATE TABLE vattu (
    Ma_Vattu NVARCHAR(50) NOT NULL,
    Ten_vattu NVARCHAR(50),
    Soluong_vattu INT,
    Gia FLOAT,
    sl_hong INT,
    Ngay_Nhap DATE,
    CONSTRAINT PK_vattu PRIMARY KEY (Ma_Vattu)
);

-- Table: nguyenlieu
CREATE TABLE nguyenlieu (
    Ma_Nglieu NVARCHAR(50) NOT NULL,
    Ten_Nglieu NVARCHAR(50),
    Soluong_NL INT,
    Gia FLOAT,
    Soluong_NLin INT,
    Date_Nglieu DATE,
    CONSTRAINT PK_nguyenlieu PRIMARY KEY (Ma_Nglieu)
);

-- Table: loaimon
CREATE TABLE loaimon (
    Ma_LM NVARCHAR(50) NOT NULL,
    Ten_LM NVARCHAR(50),
    CONSTRAINT PK_loaimon PRIMARY KEY (Ma_LM)
);

-- Table: mon
CREATE TABLE mon (
    Ma_Mon NVARCHAR(50) NOT NULL,
    Ten_mon NVARCHAR(50),
    Gia_mon FLOAT,
    Ma_LM NVARCHAR(50),
    Hinhanh NVARCHAR(50),
    CONSTRAINT PK_mon PRIMARY KEY (Ma_Mon),
    CONSTRAINT FK_mon_loaimon FOREIGN KEY (Ma_LM) REFERENCES loaimon (Ma_LM)
);

-- Table: bill
CREATE TABLE bill (
    Ma_Bill NVARCHAR(50) NOT NULL,
    Ma_NV NVARCHAR(50) NOT NULL,
    tonggia FLOAT DEFAULT 0,
    ban INT NOT NULL,
    [date] DATETIME,
    Trangthai NVARCHAR(50),
    CONSTRAINT PK_bill PRIMARY KEY (Ma_Bill),
    CONSTRAINT FK_bill_nhanvien FOREIGN KEY (Ma_NV) REFERENCES nhanvien (Ma_NV),
    CONSTRAINT FK_bill_ban FOREIGN KEY (ban) REFERENCES ban (Maban)
);

-- Table: [order]
CREATE TABLE [order] (
    Ma_Bill NVARCHAR(50) NOT NULL,
    Ma_Mon NVARCHAR(50) NOT NULL,
    soluong INT,
    Gia FLOAT,
    Thanhtien FLOAT,
    CONSTRAINT PK_order PRIMARY KEY (Ma_Bill, Ma_Mon),
    CONSTRAINT FK_order_bill FOREIGN KEY (Ma_Bill) REFERENCES bill (Ma_Bill),
    CONSTRAINT FK_order_mon FOREIGN KEY (Ma_Mon) REFERENCES mon (Ma_Mon)
);

-- Table: nhapnguyenlieu
CREATE TABLE nhapnguyenlieu (
    Ma_Nhap NVARCHAR(50) NOT NULL,
    Ma_Nguyenlieu NVARCHAR(50),
    Gia FLOAT,
    TongTien FLOAT,
    Ngaynhap DATE,
    Soluong INT,
    CONSTRAINT PK_nhapnguyenlieu PRIMARY KEY (Ma_Nhap),
    CONSTRAINT FK_nhap_nguyenlieu FOREIGN KEY (Ma_Nguyenlieu) REFERENCES nguyenlieu (Ma_Nglieu)
);

-- Table: haohut
CREATE TABLE haohut (
    Ma_Hao NVARCHAR(50) NOT NULL,
    Ma_Vattu NVARCHAR(50) NOT NULL,
    Tenvattu NVARCHAR(50),
    Gia_vattu FLOAT DEFAULT 0,
    Date_Nhap DATE,
    Tonggia_VT FLOAT DEFAULT 0,
    SL_VT INT,
    CONSTRAINT PK_haohut PRIMARY KEY (Ma_Hao),
    CONSTRAINT FK_haohut_vattu FOREIGN KEY (Ma_Vattu) REFERENCES vattu (Ma_Vattu)
);

-- Table: chebien
CREATE TABLE chebien (
    Ma_Mon NVARCHAR(50) NOT NULL,
    Ma_Nglieu NVARCHAR(50) NOT NULL,
    Soluong_chebien INT,
    CONSTRAINT PK_chebien PRIMARY KEY (Ma_Mon, Ma_Nglieu),
    CONSTRAINT FK_chebien_mon FOREIGN KEY (Ma_Mon) REFERENCES mon (Ma_Mon),
    CONSTRAINT FK_chebien_nguyenlieu FOREIGN KEY (Ma_Nglieu) REFERENCES nguyenlieu (Ma_Nglieu)
);

-- Table: ca
CREATE TABLE ca (
    Ma_ca NVARCHAR(50) NOT NULL,
    Ngay DATE NOT NULL,
    Time_start DATETIME,
    Time_end DATETIME,
    Luong_Ca FLOAT,
    Soluong_ca INT,
    Soluong_dky INT,
    CONSTRAINT PK_ca PRIMARY KEY (Ma_ca)
);

-- Table: chia_lich_lam_viec
CREATE TABLE chia_lich_lam_viec (
    Ma_phanlich NVARCHAR(50) NOT NULL,
    Ma_Nhanvien NVARCHAR(50),
    Ngay DATE,
    Ca NVARCHAR(50),
    Luong FLOAT,
    Trang_thai TINYINT,
    CONSTRAINT PK_chia_lich PRIMARY KEY (Ma_phanlich),
    CONSTRAINT FK_lich_nv FOREIGN KEY (Ma_Nhanvien) REFERENCES nhanvien (Ma_NV),
    CONSTRAINT FK_lich_ca FOREIGN KEY (Ca) REFERENCES ca (Ma_ca)
);

-- Table: doi_ca
CREATE TABLE doi_ca (
    Ma_doica NVARCHAR(50) NOT NULL,
    Ma_Phanlich1 NVARCHAR(50) NOT NULL,
    Ma_Phanlich2 NVARCHAR(50) NOT NULL,
    Ma_NV1 NVARCHAR(50) NOT NULL,
    Ma_NV2 NVARCHAR(50) NOT NULL,
    Trang_thai INT,
    CONSTRAINT PK_doica PRIMARY KEY (Ma_doica)
);

