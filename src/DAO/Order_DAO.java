/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author ADMIN
 */

import Lop.Order;
import JDBc.JDBC_connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order_DAO {

    // Thêm order mới
    public void insert(Order o) {
        String sql = "INSERT INTO `order` (Ma_Bill, Ma_Mon, soluong, Gia, Thanhtien) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, o.getMa_Bill());
            ps.setString(2, o.getMa_Mon());
            ps.setInt(3, o.getSL_order());
            ps.setDouble(4, o.getGia_Mon());
            ps.setDouble(5, o.gettonggia_order());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật order (theo Ma_Bill & Ma_Mon)
    public void update(Order o) {
        String sql = "UPDATE `order` SET soluong = ?, Gia = ?, Thanhtien = ? WHERE Ma_Bill = ? AND Ma_Mon = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getSL_order());
            ps.setDouble(2, o.getGia_Mon());
            ps.setDouble(3, o.gettonggia_order());
            ps.setString(4, o.getMa_Bill());
            ps.setString(5, o.getMa_Mon());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá order (theo Ma_Bill & Ma_Mon)
    public void delete(String maBill, String maMon) {
        String sql = "DELETE FROM `order` WHERE Ma_Bill = ? AND Ma_Mon = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBill);
            ps.setString(2, maMon);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả các order
    public List<Order> selectAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `order`";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Order o = new Order();
                o.setMa_Bill(rs.getString("Ma_Bill"));
                o.setMa_Mon(rs.getString("Ma_Mon"));
                o.setSL_order(rs.getInt("soluong"));
                o.setGia_Mon(rs.getDouble("Gia"));
                o.settonggia_order(rs.getDouble("Thanhtien"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách order theo mã hóa đơn
    public List<Order> selectByMaBill(String maBill) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE Ma_Bill = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBill);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setMa_Bill(rs.getString("Ma_Bill"));
                o.setMa_Mon(rs.getString("Ma_Mon"));
                o.setSL_order(rs.getInt("soluong"));
                o.setGia_Mon(rs.getDouble("Gia"));
                o.settonggia_order(rs.getDouble("Thanhtien"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public double tinhTongThanhTien(String maBill) {
    double tong = 0;
    String sql = "SELECT SUM(Thanhtien) AS TongTien FROM `order` WHERE Ma_Bill = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maBill);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            tong = rs.getDouble("TongTien");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tong;
}
    
}