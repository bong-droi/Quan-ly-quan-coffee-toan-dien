package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Lop.Bill;
import JDBc.JDBC_connect;

public class Bill_DAO {

    // Thêm hóa đơn mới
    public void insert(Bill bill) {
        String sql = "INSERT INTO bill (Ma_Bill, Ma_NV, tonggia, ban, date, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bill.getMa_Bill());
            ps.setString(2, bill.getMa_NV());
            ps.setDouble(3, bill.getTongGia());
            ps.setInt(4, bill.getBan());
            if (bill.getDate_Bill() != null) {
    ps.setDate(5, new java.sql.Date(bill.getDate_Bill().getTime()));
} else {
    ps.setDate(5, null);
}
            ps.setString(6, bill.getTrangThai());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật hóa đơn
    public void update(Bill bill) {
        String sql = "UPDATE bill SET Ma_NV = ?, tonggia = ?, ban = ?, date = ?, trangthai = ? WHERE Ma_Bill = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bill.getMa_NV());
            ps.setDouble(2, bill.getTongGia());
            ps.setInt(3, bill.getBan());
            if (bill.getDate_Bill() != null) {
    ps.setDate(4, new java.sql.Date(bill.getDate_Bill().getTime()));
} else {
    ps.setDate(4, null);
}
            ps.setString(5, bill.getTrangThai());
            ps.setString(6, bill.getMa_Bill());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa hóa đơn
    public void delete(String maBill) {
        String sql = "DELETE FROM bill WHERE Ma_Bill = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBill);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ danh sách hóa đơn
    public List<Bill> selectAll() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setMa_Bill(rs.getString("Ma_Bill"));
                bill.setMa_NV(rs.getString("Ma_NV"));
                bill.setTongGia(rs.getDouble("tonggia"));
                bill.setBan(rs.getInt("ban"));
                bill.setDate_Bill(rs.getDate("date"));
                bill.setTrangThai(rs.getString("trangthai"));
                list.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo mã hóa đơn
    public Bill selectById(String maBill) {
        Bill bill = null;
        String sql = "SELECT * FROM bill WHERE Ma_Bill = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBill);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bill = new Bill();
                bill.setMa_Bill(rs.getString("Ma_Bill"));
                bill.setMa_NV(rs.getString("Ma_NV"));
                bill.setTongGia(rs.getDouble("tonggia"));
                bill.setBan(rs.getInt("ban"));
                bill.setDate_Bill(rs.getDate("date"));
                bill.setTrangThai(rs.getString("trangthai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }
    public List<Bill> selectByDate(java.util.Date date) {
    List<Bill> list = new ArrayList<>();
    String sql = "SELECT * FROM bill WHERE date = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(date.getTime()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setMa_Bill(rs.getString("Ma_Bill"));
            bill.setMa_NV(rs.getString("Ma_NV"));
            bill.setTongGia(rs.getDouble("tonggia"));
            bill.setBan(rs.getInt("ban"));
            bill.setDate_Bill(rs.getDate("date"));
            bill.setTrangThai(rs.getString("trangthai"));
            list.add(bill);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public double getTongDoanhThu() {
    double tong = 0;
    String sql = "SELECT SUM(tonggia) FROM bill WHERE trangthai = 'Đã thanh toán'";
    try (Connection con = JDBC_connect.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
            tong = rs.getDouble(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tong;
}
public List<Bill> selectByStatus(String trangthai) {
    List<Bill> list = new ArrayList<>();
    String sql = "SELECT * FROM bill WHERE trangthai = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, trangthai);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setMa_Bill(rs.getString("Ma_Bill"));
            bill.setMa_NV(rs.getString("Ma_NV"));
            bill.setTongGia(rs.getDouble("tonggia"));
            bill.setBan(rs.getInt("ban"));
            bill.setDate_Bill(rs.getDate("date"));
            bill.setTrangThai(rs.getString("trangthai"));
            list.add(bill);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
public List<Bill> selectByMonth(int thang, int nam) {
    List<Bill> list = new ArrayList<>();
    String sql = "SELECT * FROM bill WHERE MONTH(date) = ? AND YEAR(date) = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setMa_Bill(rs.getString("Ma_Bill"));
            bill.setMa_NV(rs.getString("Ma_NV"));
            bill.setTongGia(rs.getDouble("tonggia"));
            bill.setBan(rs.getInt("ban"));
            bill.setDate_Bill(rs.getDate("date"));
            bill.setTrangThai(rs.getString("trangthai"));
            list.add(bill);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
public List<Bill> selectByMaNV(String maNV) {
    List<Bill> list = new ArrayList<>();
    String sql = "SELECT * FROM bill WHERE Ma_NV = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maNV);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setMa_Bill(rs.getString("Ma_Bill"));
            bill.setMa_NV(rs.getString("Ma_NV"));
            bill.setTongGia(rs.getDouble("tonggia"));
            bill.setBan(rs.getInt("ban"));
            bill.setDate_Bill(rs.getDate("date"));
            bill.setTrangThai(rs.getString("trangthai"));
            list.add(bill);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
}
