/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;

/**
 *
 * @author ADMIN
 */
public class Order {
    private String Ma_Bill;
    private String Ma_Mon;
    private int SL_order;
    private double Gia_Mon;
    private double tonggia_order;

    public Order() {
    }

    public Order(String Ma_Bill, String Ma_Mon, int SL_order, double Gia_Mon, double tonggia_order) {
        this.Ma_Bill = Ma_Bill;
        this.Ma_Mon = Ma_Mon;
        this.SL_order = SL_order;
        this.Gia_Mon = Gia_Mon;
        this.tonggia_order = tonggia_order;
    }
    
    
    public String getMa_Mon() {
        return Ma_Mon;
    }

    public void setMa_Mon(String Ma_Mon) {
        this.Ma_Mon = Ma_Mon;
    }
    public double getGia_Mon() {
        return Gia_Mon;
    }

    public void setGia_Mon(double Gia_Mon) {
        this.Gia_Mon = Gia_Mon;
    }
    public String getMa_Bill() {
        return Ma_Bill;
    }

    public void setMa_Bill(String Ma_Bill) {
        this.Ma_Bill = Ma_Bill;
    }
    public double gettonggia_order() {
        return tonggia_order;
    }
    public void settonggia_order(double tonggia_order) {
        this.tonggia_order = tonggia_order;
    }

    public int getSL_order() {
       return SL_order; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public void setSL_order(int SL_order){
        this.SL_order=SL_order;
    }
}
