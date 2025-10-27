/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import JDBc.JDBC_connect;
import Lop.Haohut;
import JDBc.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Haohut_DAO {
    public void Insert(Haohut t) {
    String sql = "INSERT INTO haohut (Ma_Hao, Ma_Vattu, Tenvattu, Gia_vattu, Date_Nhap, Tonggia_VT, SL_VT) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, t.getMa_Hao());
        ps.setString(2, t.getMa_Vattu());
        ps.setString(3, t.getTen_VT());
        ps.setDouble(4, t.getGia_VT());
        ps.setDate(5, new java.sql.Date(t.getdate_invt().getTime()));
        ps.setDouble(6, t.getTonggia_VT());
        ps.setInt(7, t.getSL_VT());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public void Delete(String maHao) {
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "DELETE FROM haohut WHERE Ma_Hao = '" + maHao + "'";
        st.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void Update(Haohut t) {
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = sdf.format(t.getdate_invt());

        String sql = "UPDATE vattu SET "
                + "Ma_Vattu = '" + t.getMa_Vattu() + "', "
                + "tenvattu = '" + t.getTen_VT() + "', "
                + "Gia_vattu = " + t.getGia_VT() + ", "
                + "Date_Nhap = '" + dateFormatted + "', "
                + "Tonggia_VT = " + t.getTonggia_VT() + ", "
                + "SL_VT = " + t.getSL_VT()
                + " WHERE Ma_Hao = '" + t.getMa_Hao() + "'";

        st.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public List<Haohut> selectAll() {
    List<Haohut> list = new ArrayList<>();
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "SELECT * FROM haohut";
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Haohut t = new Haohut();
            t.setMa_Hao(rs.getString("Ma_Hao"));
            t.setMa_Vattu(rs.getString("Ma_Vattu"));
            t.setTen_VT(rs.getString("tenvattu"));
            t.setGia_VT(rs.getDouble("Gia_vattu"));
            t.setdate_invt(rs.getDate("Date_Nhap"));
            t.setTonggia_VT(rs.getDouble("Tonggia_VT"));
            t.setSL_VT(rs.getInt("SL_VT"));
            list.add(t);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public Haohut selectByMa_Hao(String maHao) {
    Haohut t = null;
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "SELECT * FROM haohut WHERE Ma_Hao = '" + maHao + "'";
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            t = new Haohut();
            t.setMa_Hao(rs.getString("Ma_Hao"));
            t.setMa_Vattu(rs.getString("Ma_Vattu"));
            t.setTen_VT(rs.getString("tenvattu"));
            t.setGia_VT(rs.getDouble("Gia_vattu"));
            t.setdate_invt(rs.getDate("Date_Nhap"));
            t.setTonggia_VT(rs.getDouble("Tonggia_VT"));
            t.setSL_VT(rs.getInt("SL_VT"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return t;
}
    public List<Haohut> selectByDate(java.util.Date date) {
    List<Haohut> list = new ArrayList<>();
    String sql = "SELECT * FROM haohut WHERE DATE(Date_Nhap) = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(date.getTime()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Haohut t = new Haohut();
            t.setMa_Hao(rs.getString("Ma_Hao"));
            t.setMa_Vattu(rs.getString("Ma_Vattu"));
            t.setTen_VT(rs.getString("tenvattu"));
            t.setGia_VT(rs.getDouble("Gia_vattu"));
            t.setdate_invt(rs.getDate("Date_Nhap"));
            t.setTonggia_VT(rs.getDouble("Tonggia_VT"));
            t.setSL_VT(rs.getInt("SL_VT"));
            list.add(t);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
public List<Haohut> selectByMonthYear(int month, int year) {
    List<Haohut> list = new ArrayList<>();
    String sql = "SELECT * FROM haohut WHERE MONTH(Date_Nhap) = ? AND YEAR(Date_Nhap) = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, month);
        ps.setInt(2, year);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Haohut t = new Haohut();
            t.setMa_Hao(rs.getString("Ma_Hao"));
            t.setMa_Vattu(rs.getString("Ma_Vattu"));
            t.setTen_VT(rs.getString("tenvattu"));
            t.setGia_VT(rs.getDouble("Gia_vattu"));
            t.setdate_invt(rs.getDate("Date_Nhap"));
            t.setTonggia_VT(rs.getDouble("Tonggia_VT"));
            t.setSL_VT(rs.getInt("SL_VT"));
            list.add(t);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}
