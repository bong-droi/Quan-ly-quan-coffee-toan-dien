/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Lop;

/**
 *
 * @author ADMIN
 */
public class LoaiMon {
    

    private String Ma_LM;
    private String Ten_LM;
    
    @Override
    public String toString() {
        return this.Ma_LM;
    }

    public LoaiMon() {
    }

    public LoaiMon(String Ma_LM, String Ten_LM) {
        this.Ma_LM = Ma_LM;
        this.Ten_LM= Ten_LM;
    }

    public String getMa_LM() {
        return Ma_LM;
    }

    public void setMa_LM(String Ma_LM) {
        this.Ma_LM = Ma_LM;
    }

    public String getTen_LM() {
        return Ten_LM;
    }

    public void setTen_LM(String Ten_LM) {
        this.Ten_LM = Ten_LM;
    }

}
    

