/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import JDBc.JDBC_connect;
import Lop.Mon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class Mon_DAO {
    public boolean Insert(Mon m) {
    String sql = "INSERT INTO mon (Ma_Mon, Ten_mon, Gia_mon, Ma_LM, Hinhanh) VALUES (?, ?, ?, ?, ?)";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, m.getMa_Mon());
        ps.setString(2, m.getTen_Mon());
        ps.setDouble(3, m.getGia_Mon());
        ps.setString(4, m.getMa_LM());
        ps.setString(5, m.getHinhanh());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public void Update(Mon t) {
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "UPDATE mon SET "
                   + "Ten_mon = '" + t.getTen_Mon() + "', "
                   + "Gia_mon = " + t.getGia_Mon() + ", "
                   + "Ma_LM = '" + t.getMa_LM() + "', "
                   + "Hinhanh = '" + t.getHinhanh() + "' "
                   + "WHERE Ma_Mon = '" + t.getMa_Mon() + "'";

        st.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    public void Delete(String maMon) {
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "DELETE FROM mon WHERE Ma_Mon = '" + maMon + "'";
        st.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public List<Mon> selectAll() {
    List<Mon> list = new ArrayList<>();
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "SELECT * FROM mon";
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            String maMon = rs.getString("Ma_Mon");
            String tenMon = rs.getString("Ten_mon");
            double giaMon = rs.getDouble("Gia_mon");
            String maLM = rs.getString("Ma_LM");
            String hinhAnh = rs.getString("Hinhanh");

            Mon m = new Mon(maMon, tenMon, giaMon, maLM, hinhAnh);
            list.add(m);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public Mon selectByMa_Mon(String maMon) {
    Mon m = null;
    try {
        Connection con = JDBC_connect.getConnection();
        Statement st = con.createStatement();

        String sql = "SELECT * FROM mon WHERE Ma_Mon = '" + maMon + "'";
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            String tenMon = rs.getString("Ten_mon");
            double giaMon = rs.getDouble("Gia_mon");
            String maLM = rs.getString("Ma_LM");
            String hinhAnh = rs.getString("Hinhanh");

            m = new Mon(maMon, tenMon, giaMon, maLM, hinhAnh);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return m;
}
public List<Mon> selectByMaLoai(String maLoai) {
    List<Mon> list = new ArrayList<>();
    try {
        Connection con = JDBC_connect.getConnection();
        String sql = "SELECT * FROM mon WHERE Ma_LM = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, maLoai);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String maMon = rs.getString("Ma_Mon");
            String tenMon = rs.getString("Ten_mon");
            double giaMon = rs.getDouble("Gia_mon");
            String maLM = rs.getString("Ma_LM");
            String hinhAnh = rs.getString("Hinhanh");

            Mon m = new Mon(maMon, tenMon, giaMon, maLM, hinhAnh);
            list.add(m);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    
    
    
}
