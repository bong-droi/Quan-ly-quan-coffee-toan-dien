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
public class Nguyenlieu {
    private String Ma_Nglieu;
    private String ten_Nglieu;
    private double SL_NL;
    private double gia_Nglieu;
    private int SL_NLin;
    private Date date_Nglieu;
    @Override
    public String toString() {
        return this.Ma_Nglieu;
    }
    // Constructor không đối số
    public Nguyenlieu() {
    }

    // Constructor đầy đủ
    public Nguyenlieu(String ma_Nglieu, String ten_Nglieu, int SL_NL, double gia_Nglieu, int SL_NLin, Date date_Nglieu) {
        this.Ma_Nglieu = ma_Nglieu;
        this.ten_Nglieu = ten_Nglieu;
        this.SL_NL = SL_NL;
        this.gia_Nglieu = gia_Nglieu;
        this.SL_NLin = SL_NLin;
        this.date_Nglieu = date_Nglieu;
    }

    // Getters và Setters
    public String getMa_Nglieu() {
        return Ma_Nglieu;
    }

    public void setMa_Nglieu(String ma_Nglieu) {
        this.Ma_Nglieu = ma_Nglieu;
    }

    public String getTen_Nglieu() {
        return ten_Nglieu;
    }

    public void setTen_Nglieu(String ten_Nglieu) {
        this.ten_Nglieu = ten_Nglieu;
    }

    public double getSL_NL() {
        return SL_NL;
    }

    public void setSL_NL(double SL_NL) {
        this.SL_NL = SL_NL;
    }

    public double getGia_Nglieu() {
        return gia_Nglieu;
    }

    public void setGia_Nglieu(double gia_Nglieu) {
        this.gia_Nglieu = gia_Nglieu;
    }

    public int getSL_NLin() {
        return SL_NLin;
    }

    public void setSL_NLin(int SL_NLin) {
        this.SL_NLin = SL_NLin;
    }

    public Date getDate_Nglieu() {
        return date_Nglieu;
    }

    public void setDate_Nglieu(Date date_Nglieu) {
        this.date_Nglieu = date_Nglieu;
    }
    
}
