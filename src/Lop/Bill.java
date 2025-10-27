package Lop;

import java.util.Date;

public class Bill {
    private String Ma_Bill;
    private String Ma_NV;
    private double tonggia;
    private int ban;
    private Date date_bill;
    private String trangthai;

    public Bill() {}

    public Bill(String Ma_Bill, String Ma_NV, double tonggia, int ban, Date date_bill, String trangthai) {
        this.Ma_Bill = Ma_Bill;
        this.Ma_NV = Ma_NV;
        this.tonggia = tonggia;
        this.ban = ban;
        this.date_bill = date_bill;
        this.trangthai = trangthai;
    }

    @Override
    public String toString() {
        return Ma_Bill + " - " + trangthai;
    }

    public String getMa_Bill() {
        return Ma_Bill;
    }

    public void setMa_Bill(String Ma_Bill) {
        this.Ma_Bill = Ma_Bill;
    }

    public String getMa_NV() {
        return Ma_NV;
    }

    public void setMa_NV(String Ma_NV) {
        this.Ma_NV = Ma_NV;
    }

    public double getTongGia() {
        return tonggia;
    }

    public void setTongGia(double tonggia) {
        this.tonggia = tonggia;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public Date getDate_Bill() {
        return date_bill;
    }

    public void setDate_Bill(Date date_bill) {
        this.date_bill = date_bill;
    }

    public String getTrangThai() {
        return trangthai;
    }

    public void setTrangThai(String trangthai) {
        this.trangthai = trangthai;
    }
}
