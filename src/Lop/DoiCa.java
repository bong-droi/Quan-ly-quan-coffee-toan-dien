/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;

public class DoiCa {
    private String maDoiCa;
    private String maPhanLich1;
    private String maPhanLich2;
    private String maNV1;
    private String maNV2;

    public DoiCa() {}

    public DoiCa(String maDoiCa, String maPhanLich1, String maPhanLich2, String maNV1, String maNV2) {
        this.maDoiCa = maDoiCa;
        this.maPhanLich1 = maPhanLich1;
        this.maPhanLich2 = maPhanLich2;
        this.maNV1 = maNV1;
        this.maNV2 = maNV2;
    }

    public String getMaDoiCa() {
        return maDoiCa;
    }

    public void setMaDoiCa(String maDoiCa) {
        this.maDoiCa = maDoiCa;
    }

    public String getMaPhanLich1() {
        return maPhanLich1;
    }

    public void setMaPhanLich1(String maPhanLich1) {
        this.maPhanLich1 = maPhanLich1;
    }

    public String getMaPhanLich2() {
        return maPhanLich2;
    }

    public void setMaPhanLich2(String maPhanLich2) {
        this.maPhanLich2 = maPhanLich2;
    }

    public String getMaNV1() {
        return maNV1;
    }

    public void setMaNV1(String maNV1) {
        this.maNV1 = maNV1;
    }

    public String getMaNV2() {
        return maNV2;
    }

    public void setMaNV2(String maNV2) {
        this.maNV2 = maNV2;
    }
    private int trangThai;

public DoiCa(String maDoiCa, String maPhanLich1, String maPhanLich2, String maNV1, String maNV2, int trangThai) {
    this.maDoiCa = maDoiCa;
    this.maPhanLich1 = maPhanLich1;
    this.maPhanLich2 = maPhanLich2;
    this.maNV1 = maNV1;
    this.maNV2 = maNV2;
    this.trangThai = trangThai;
}

public int getTrangThai() {
    return trangThai;
}

public void setTrangThai(int trangThai) {
    this.trangThai = trangThai;
}
}

