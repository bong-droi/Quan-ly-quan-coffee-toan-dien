package Lop;


public class Ban {
    private int Maban;
    private String Trangthai;

    public Ban() {}

    public Ban(int Maban, String Trangthai) {
        this.Maban = Maban;
        this.Trangthai = Trangthai;
    }

    public int getMaban() {
        return Maban;
    }

    public void setMaban(int Maban) {
        this.Maban = Maban;
    }

    public String getTrangthai() {
        return Trangthai;
    }

    public void setTrangthai(String Trangthai) {
        this.Trangthai = Trangthai;
    }

    @Override
    public String toString() {
        return "Bàn " + Maban + " - " + (Trangthai != null ? Trangthai : "Chưa rõ");
    }
}
