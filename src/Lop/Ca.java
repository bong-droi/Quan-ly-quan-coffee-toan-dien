package Lop;

import java.util.Date;

public class Ca {
    private String maCa;
    private Date timeStart;
    private Date timeEnd;
    private double luongCa;
    private int soluongCa;
    private int soluongDky;

    // Getters and Setters
    private java.sql.Date ngay;

public java.sql.Date getNgay() {
    return ngay;
}

public void setNgay(java.sql.Date ngay) {
    this.ngay = ngay;
}

    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getLuongCa() {
        return luongCa;
    }

    public void setLuongCa(double luongCa) {
        this.luongCa = luongCa;
    }

    public int getSoluongCa() {
        return soluongCa;
    }

    public void setSoluongCa(int soluongCa) {
        this.soluongCa = soluongCa;
    }

    public int getSoluongDky() {
        return soluongDky;
    }

    public void setSoluongDky(int soluongDky) {
        this.soluongDky = soluongDky;
    }
}
