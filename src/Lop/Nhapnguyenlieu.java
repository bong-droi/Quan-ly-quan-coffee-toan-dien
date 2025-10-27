/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;
import java.util.Date;
/**
 *
 * @author ADMIN
 */
public class Nhapnguyenlieu {
    private String maNhap;
    private String maNguyenlieu;
    private Double gia;
    private Double tongTien;
    private Date ngayNhap;
    private Double soLuong;

    // Constructors
    public Nhapnguyenlieu() {}

    public Nhapnguyenlieu(String maNhap, String maNguyenlieu, Double gia, Double tongTien, Date ngayNhap, Double soLuong) {
        this.maNhap = maNhap;
        this.maNguyenlieu = maNguyenlieu;
        this.gia = gia;
        this.tongTien = tongTien;
        this.ngayNhap = ngayNhap;
        this.soLuong = soLuong;
    }

    // Getters and Setters
    public String getMaNhap() {
        return maNhap;
    }

    public void setMaNhap(String maNhap) {
        this.maNhap = maNhap;
    }

    public String getMaNguyenlieu() {
        return maNguyenlieu;
    }

    public void setMaNguyenlieu(String maNguyenlieu) {
        this.maNguyenlieu = maNguyenlieu;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public Double getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Double soLuong) {
        this.soLuong = soLuong;
    }
    
}
