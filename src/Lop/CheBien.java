package Lop;

/**
 *
 * @author ADMIN
 */
public class CheBien {
    private String Ma_Mon;
    private String Ma_Nglieu;
    private double SL_chebien;

    // Constructor không đối số
    public CheBien() {
    }

    // Constructor đầy đủ tham số
    public CheBien(String Ma_Mon, String Ma_Nglieu, int SL_chebien) {
        this.Ma_Mon = Ma_Mon;
        this.Ma_Nglieu = Ma_Nglieu;
        this.SL_chebien = SL_chebien;
    }

    public String getMa_Mon() {
        return Ma_Mon;
    }

    public void setMa_Mon(String Ma_Mon) {
        this.Ma_Mon = Ma_Mon;
    }

    public String getMa_Nglieu() {
        return Ma_Nglieu;
    }

    public void setMa_Nglieu(String Ma_Nglieu) {
        this.Ma_Nglieu = Ma_Nglieu;
    }

    public double getSL_chebien() {
        return SL_chebien;
    }

    public void setSL_chebien(double SL_chebien) {
        this.SL_chebien = SL_chebien;
    }

    public Object getMaMon() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
