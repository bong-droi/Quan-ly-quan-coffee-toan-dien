/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Lop.Nhanvien;
import JDBc.JDBC_connect;
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
public class Nhanvien_DAO {
    
    public void Insert(Nhanvien t) {
    try {
        Connection con = JDBC_connect.getConnection();
        String sql = "INSERT INTO nhanvien (Ma_NV, Ten, CCCD, SDT, Birth, Gioitinh, Role, Phat,  user, pass) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, t.getMa_NV());
        ps.setString(2, t.getTen_NV());
        ps.setString(3, t.getCCCD());
        ps.setString(4, t.getSDT());
        ps.setDate(5, new java.sql.Date(t.getBirth().getTime()));
        ps.setBoolean(6, t.getGT());
        ps.setBoolean(7, t.getRole());
        ps.setDouble(8, t.getluong());
        
        ps.setString(9, t.getUser());
        ps.setString(10, t.getPass());

        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    public void Update(Nhanvien t) {
    try {
        Connection con = JDBC_connect.getConnection();
        String sql = "UPDATE nhanvien SET "
                   + "Ten = ?, CCCD = ?, SDT = ?, Birth = ?, Gioitinh = ?, Role = ?, Phat = ?,  user = ?, pass = ? "
                   + "WHERE Ma_NV = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, t.getTen_NV());
        ps.setString(2, t.getCCCD());
        ps.setString(3, t.getSDT());
        ps.setDate(4, new java.sql.Date(t.getBirth().getTime()));
        ps.setBoolean(5, t.getGT());
        ps.setBoolean(6, t.getRole());
        ps.setDouble(7, t.getluong());      
        ps.setString(8, t.getUser());
        ps.setString(9, t.getPass());
        ps.setString(10, t.getMa_NV());

        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

  public void Delete(Nhanvien t) {
    try {
        Connection con = JDBC_connect.getConnection();
        String sql = "DELETE FROM nhanvien WHERE Ma_NV = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, t.getMa_NV());

        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public ArrayList<Nhanvien>SelectAll()
    {   
        ArrayList<Nhanvien> list = new ArrayList<>();
        try {
            Connection con = JDBC_connect.getConnection();
            Statement st = con.createStatement();

            String sql = "SELECT * FROM `nhanvien`";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Nhanvien nv = new Nhanvien();
                nv.setMa_NV(rs.getString("Ma_NV"));
                nv.setTen_NV(rs.getString("Ten"));
                nv.setCCCD(rs.getString("CCCD"));
                nv.setSDT(rs.getString("SDT"));
                nv.setBirth(rs.getDate("Birth"));
                nv.setGT(rs.getBoolean("Gioitinh"));
                nv.setRole(rs.getBoolean("Role"));
                nv.setluong(rs.getDouble("Phat"));
                
                nv.setUser(rs.getString("user"));
                nv.setPass(rs.getString("pass"));

                list.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    
    }
    public Nhanvien SelectByMa_NV(String maNV) {
    Nhanvien nv = null;
    String sql = "SELECT * FROM `nhanvien` WHERE Ma_NV = ?";

    try (
        Connection con = JDBC_connect.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
    ) {
        ps.setString(1, maNV);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            nv = new Nhanvien();
            nv.setMa_NV(rs.getString("Ma_NV"));
            nv.setTen_NV(rs.getString("Ten"));
            nv.setCCCD(rs.getString("CCCD"));
            nv.setSDT(rs.getString("SDT"));
            nv.setBirth(rs.getDate("Birth"));
            nv.setGT(rs.getBoolean("Gioitinh"));
            nv.setRole(rs.getBoolean("Role"));
            nv.setluong(rs.getDouble("Phat"));
            
            nv.setUser(rs.getString("user"));
            nv.setPass(rs.getString("pass"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nv;
}
   public ArrayList<Nhanvien> SelectByCondition(String condition, List<Object> parameters) {
    ArrayList<Nhanvien> list = new ArrayList<>();
    try {
        Connection con = JDBC_connect.getConnection();
        String sql = "SELECT * FROM `nhanvien` WHERE " + condition;
        PreparedStatement ps = con.prepareStatement(sql);

        // Gán tham số vào dấu ? trong câu SQL
        for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(i + 1, parameters.get(i));
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Nhanvien nv = new Nhanvien();
            nv.setMa_NV(rs.getString("Ma_NV"));
            nv.setTen_NV(rs.getString("Ten"));
            nv.setCCCD(rs.getString("CCCD"));
            nv.setSDT(rs.getString("SDT"));
            nv.setBirth(rs.getDate("Birth"));
            nv.setGT(rs.getBoolean("Gioitinh"));
            nv.setRole(rs.getBoolean("Role"));
            nv.setluong(rs.getDouble("Phat"));
            
            nv.setUser(rs.getString("user"));
            nv.setPass(rs.getString("pass"));

            list.add(nv);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
   public Nhanvien SelectByUser(String username) {
    Nhanvien nv = null;
    String sql = "SELECT * FROM `nhanvien` WHERE `user` = ?";

    try (
        Connection con = JDBC_connect.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
    ) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            nv = new Nhanvien();
            nv.setMa_NV(rs.getString("Ma_NV"));
            nv.setTen_NV(rs.getString("Ten"));
            nv.setCCCD(rs.getString("CCCD"));
            nv.setSDT(rs.getString("SDT"));
            nv.setBirth(rs.getDate("Birth"));
            nv.setGT(rs.getBoolean("Gioitinh"));
            nv.setRole(rs.getBoolean("Role"));
            nv.setluong(rs.getDouble("Phat"));
            
            nv.setUser(rs.getString("user"));
            nv.setPass(rs.getString("pass"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nv;
}
public Nhanvien kiemTraDangNhap(String username, String password) {
    Nhanvien nv = null;
    String sql = "SELECT * FROM `nhanvien` WHERE `user` = ? AND `pass` = ?";

    try (
        Connection con = JDBC_connect.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
    ) {
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            nv = new Nhanvien();
            nv.setMa_NV(rs.getString("Ma_NV"));
            nv.setTen_NV(rs.getString("Ten"));
            nv.setCCCD(rs.getString("CCCD"));
            nv.setSDT(rs.getString("SDT"));
            nv.setBirth(rs.getDate("Birth"));
            nv.setGT(rs.getBoolean("Gioitinh"));
            nv.setRole(rs.getBoolean("Role"));
            nv.setluong(rs.getDouble("Phat"));
            nv.setUser(rs.getString("user"));
            nv.setPass(rs.getString("pass"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nv;
}

    
}
