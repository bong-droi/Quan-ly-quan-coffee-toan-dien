/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;

/**
 *
 * @author ADMIN
 */
public class Mon {
    private String Ma_Mon;
    private String Ten_Mon;
    private double Gia_Mon;
    private String Ma_LM;
    private String Hinhanh;
    
    @Override
    public String toString() {
        return this.Ma_Mon;
    }

    public Mon() {
    }

    public Mon(String Ma_Mon, String Ten_Mon, double Gia_Mon, String Ma_LM, String Hinhanh) {
        this.Ma_Mon = Ma_Mon;
        this.Ten_Mon = Ten_Mon;
        this.Gia_Mon = Gia_Mon;
        this.Ma_LM = Ma_LM;
        this.Hinhanh = Hinhanh;
    }
    
    public String getHinhanh() {
        return Hinhanh;
    }

    public void setHinhanh(String Hinhanh) {
        this.Hinhanh = Hinhanh;
    }

    public String getMa_Mon() {
        return Ma_Mon;
    }

    public void setMa_Mon(String Ma_Mon) {
        this.Ma_Mon = Ma_Mon;
    }

    public String getTen_Mon() {
        return Ten_Mon;
    }

    public void setTen_Mon(String Ten_Mon) {
        this.Ten_Mon = Ten_Mon;
    }

    public double getGia_Mon() {
        return Gia_Mon;
    }

    public void setGia_Mon(double Gia_Mon) {
        this.Gia_Mon = Gia_Mon;
    }

    public String getMa_LM() {
        return Ma_LM;
    }

    public void setMa_LM(String Ma_LM) {
        this.Ma_LM = Ma_LM;
    }
    
}
