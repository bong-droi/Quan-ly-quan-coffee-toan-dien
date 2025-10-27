/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class LichLamViec {
    private String maPhanLich;
    private String maNhanVien;
    private java.sql.Date ngay;
    private String ca;
    private Double luong;
    private Integer trangThai;

    public LichLamViec() {}

    public LichLamViec(String maPhanLich, String maNhanVien, java.sql.Date ngay, String ca, Double luong, Integer trangThai) {
        this.maPhanLich = maPhanLich;
        this.maNhanVien = maNhanVien;
        this.ngay = ngay;
        this.ca = ca;
        this.luong = luong;
        this.trangThai = trangThai;
    }

    public String getMaPhanLich() {
        return maPhanLich;
    }

    public void setMaPhanLich(String maPhanLich) {
        this.maPhanLich = maPhanLich;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public java.sql.Date getNgay() {
        return ngay;
    }

    public void setNgay(java.sql.Date ngay) {
        this.ngay = ngay;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public Double getLuong() {
        return luong;
    }

    public void setLuong(Double luong) {
        this.luong = luong;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
