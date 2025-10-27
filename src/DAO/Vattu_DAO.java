/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import JDBc.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Lop.Vattu;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Vattu_DAO {

    public void Insert(Vattu t) {
        String sql = "INSERT INTO vattu (Ma_Vattu, Ten_vattu, Soluong_vattu, Gia, sl_hong, Ngay_nhap) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getMat_VT());
            ps.setString(2, t.getTen_VT());
            ps.setInt(3, t.getSL_VT());
            ps.setDouble(4, t.getGia_VT());
            ps.setInt(5, t.getSl_hong());
            ps.setDate(6, new java.sql.Date(t.getNgay_nhap().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Update(Vattu t) {
        String sql = "UPDATE vattu SET Ten_vattu=?, Soluong_vattu=?, Gia=?, sl_hong=?, Ngay_nhap=? WHERE Ma_Vattu=?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getTen_VT());
            ps.setInt(2, t.getSL_VT());
            ps.setDouble(3, t.getGia_VT());
            ps.setInt(4, t.getSl_hong());
            ps.setDate(5, new java.sql.Date(t.getNgay_nhap().getTime()));
            ps.setString(6, t.getMat_VT());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Delete(String maVT) {
        String sql = "DELETE FROM vattu WHERE Ma_Vattu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVT);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vattu> selectAll() {
        List<Vattu> list = new ArrayList<>();
        String sql = "SELECT * FROM vattu";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vattu vt = new Vattu(
                    rs.getString("Ma_Vattu"),
                    rs.getString("Ten_vattu"),
                    rs.getInt("Soluong_vattu"),
                    rs.getDouble("Gia"),
                    rs.getInt("sl_hong"),
                    rs.getDate("Ngay_nhap")
                );
                list.add(vt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Vattu selectByMa_VT(String maVT) {
        Vattu vt = null;
        String sql = "SELECT * FROM vattu WHERE Ma_Vattu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVT);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                vt = new Vattu(
                    rs.getString("Ma_Vattu"),
                    rs.getString("Ten_vattu"),
                    rs.getInt("Soluong_vattu"),
                    rs.getDouble("Gia"),
                    rs.getInt("sl_hong"),
                    rs.getDate("Ngay_nhap")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vattu findByMaVT(String maVT) {
         Vattu vt = null;
    String sql = "SELECT * FROM vattu WHERE Ma_Vattu = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maVT);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            vt = new Vattu(
                rs.getString("Ma_Vattu"),
                rs.getString("Ten_vattu"),
                rs.getInt("Soluong_vattu"),
                rs.getDouble("Gia"),
                rs.getInt("sl_hong"),
                rs.getDate("Ngay_nhap")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return vt;
   }
     
}
