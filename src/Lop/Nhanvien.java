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
public class Nhanvien {
   private String Ma_NV;
   private  String Ten_NV;
   private  String CCCD;
   private  String SDT;
   private  Date Birth;
   private  boolean  GT;
   private  boolean Role;
   private  double luong;

   private  String User;
   private  String Pass;
   
   @Override
    public String toString() {
        return this.Ten_NV;
    }

    public Nhanvien() {
    }

    public Nhanvien(String Ma_NV, String Ten_NV, String CCCD, String SDT, Date Birth, boolean GT, boolean Role, double luong, String User, String Pass) {
        this.Ma_NV = Ma_NV;
        this.Ten_NV = Ten_NV;
        this.CCCD = CCCD;
        this.SDT = SDT;
        this.Birth = Birth;
        this.GT = GT;
        this.Role = Role;
        this.luong = luong;

        this.User = User;
        this.Pass = Pass;
    }
    
    public String getMa_NV() {
        return Ma_NV;
    }
    public void setMa_NV(String Ma_NV) {
        this.Ma_NV = Ma_NV;
    }
    public String getTen_NV() {
        return Ten_NV;
    }
    public void setTen_NV(String Ten_NV) {
        this.Ten_NV = Ten_NV;
    }
    public String getCCCD() {
        return CCCD;
    }
    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }
    public String getSDT() {
        return SDT;
    }
    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
    public Date getBirth() {
        return Birth;
    }

    public void setBirth(Date Birth) {
        this.Birth = Birth;
    }
    public boolean getGT() {
        return GT;
    }

    public void setGT(boolean GT) {
        this.GT = GT;
    }
    public boolean getRole() {
        return Role;
    }

    public void setRole(boolean Role) {
        this.Role = Role;
    }
    public double getluong ()  {
        return luong;
    }
    public void setluong (double luong){
         this.luong=luong;
    }

    public String getUser() {
        return User;
    }
    public void setUser(String User) {
        this.User = User;
    }
    public String getPass() {
        return Pass;
    }
    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    
}
