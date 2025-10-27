package Lop;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Haohut {
    private String Ma_Hao;
    private String Ma_Vattu;
    private String Ten_VT;
    private double Gia_VT;
    private Date date_invt;
    private double Tonggia_VT;
    private int SL_VT;

    // Constructor không đối số
    public Haohut() {
    }

    // Constructor đầy đủ tham số
    public Haohut(String Ma_Hao, String Ma_Vattu, String Ten_VT, double Gia_VT, Date date_invt, double Tonggia_VT, int SL_VT) {
        this.Ma_Hao = Ma_Hao;
        this.Ma_Vattu = Ma_Vattu;
        this.Ten_VT = Ten_VT;
        this.Gia_VT = Gia_VT;
        this.date_invt = date_invt;
        this.Tonggia_VT = Tonggia_VT;
        this.SL_VT = SL_VT;
    }

    @Override
    public String toString() {
        return this.Ma_Hao;
    }

    public String getMa_Hao() {
        return Ma_Hao;
    }

    public void setMa_Hao(String Ma_Hao) {
        this.Ma_Hao = Ma_Hao;
    }

    public String getMa_Vattu() {
        return Ma_Vattu;
    }

    public void setMa_Vattu(String Ma_Vattu) {
        this.Ma_Vattu = Ma_Vattu;
    }

    public String getTen_VT() {
        return Ten_VT;
    }

    public void setTen_VT(String Ten_VT) {
        this.Ten_VT = Ten_VT;
    }

    public double getGia_VT() {
        return Gia_VT;
    }

    public void setGia_VT(double Gia_VT) {
        this.Gia_VT = Gia_VT;
    }

    public double getTonggia_VT() {
        return Tonggia_VT;
    }

    public void setTonggia_VT(double Tonggia_VT) {
        this.Tonggia_VT = Tonggia_VT;
    }

    public Date getdate_invt() {
        return date_invt;
    }

    public void setdate_invt(Date date_invt) {
        this.date_invt = date_invt;
    }

    public int getSL_VT() {
        return SL_VT;
    }

    public void setSL_VT(int SL_VT) {
        this.SL_VT = SL_VT;
    }
}
