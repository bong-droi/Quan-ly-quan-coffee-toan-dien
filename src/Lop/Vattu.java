package Lop;

import java.util.Date;

public class Vattu {
    private String Ma_VT;
    private String Ten_VT;
    private int SL_VT;
    private double Gia_VT;
    private int sl_hong;
    private Date Ngay_nhap;

    @Override 
    public String toString() {
        return this.Ma_VT;
    }

    public Vattu() {
    }

    public Vattu(String Ma_VT, String Ten_VT, int SL_VT, double Gia_VT, int sl_hong, Date Ngay_nhap) {
        this.Ma_VT = Ma_VT;
        this.Ten_VT = Ten_VT;
        this.SL_VT = SL_VT;
        this.Gia_VT = Gia_VT;
        this.sl_hong = sl_hong;
        this.Ngay_nhap = Ngay_nhap;
    }

    public String getMat_VT() {
        return Ma_VT;
    }

    public void setMa_VT(String Ma_VT) {
        this.Ma_VT = Ma_VT;
    }

    public String getTen_VT() {
        return Ten_VT;
    }

    public void setTen_VT(String Ten_VT) {
        this.Ten_VT = Ten_VT;
    }

    public int getSL_VT() {
        return SL_VT;
    }

    public void setSL_VT(int SL_VT) {
        this.SL_VT = SL_VT;
    }

    public double getGia_VT() {
        return Gia_VT;
    }

    public void setGia_VT(double Gia_VT) {
        this.Gia_VT = Gia_VT;
    }

    public int getSl_hong() {
        return sl_hong;
    }

    public void setSl_hong(int sl_hong) {
        this.sl_hong = sl_hong;
    }

    public Date getNgay_nhap() {
        return Ngay_nhap;
    }

    public void setNgay_nhap(Date Ngay_nhap) {
        this.Ngay_nhap = Ngay_nhap;
    }
}