/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Lop.Nhapnguyenlieu;
import JDBc.JDBC_connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class Nhapnguyenlieu_DAO {
     public void insert(Nhapnguyenlieu n) {
        String sql = "INSERT INTO nhapnguyenlieu (Ma_Nhap, Ma_Nguyenlieu, Gia, TongTien, Ngaynhap, Soluong) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getMaNhap());
            ps.setString(2, n.getMaNguyenlieu());
            ps.setDouble(3, n.getGia());
            ps.setDouble(4, n.getTongTien());
            ps.setDate(5, new java.sql.Date(n.getNgayNhap().getTime()));
            ps.setDouble(6, n.getSoLuong());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật phiếu nhập
    public void update(Nhapnguyenlieu n) {
        String sql = "UPDATE nhapnguyenlieu SET Ma_Nguyenlieu = ?, Gia = ?, TongTien = ?, Ngaynhap = ?, Soluong = ? WHERE Ma_Nhap = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getMaNguyenlieu());
            ps.setDouble(2, n.getGia());
            ps.setDouble(3, n.getTongTien());
            ps.setDate(4, new java.sql.Date(n.getNgayNhap().getTime()));
            ps.setDouble(5, n.getSoLuong());
            ps.setString(6, n.getMaNhap());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá phiếu nhập theo mã
    public void delete(String maNhap) {
        String sql = "DELETE FROM nhapnguyenlieu WHERE Ma_Nhap = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhap);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ phiếu nhập
    public List<Nhapnguyenlieu> selectAll() {
        List<Nhapnguyenlieu> list = new ArrayList<>();
        String sql = "SELECT * FROM nhapnguyenlieu";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Nhapnguyenlieu n = new Nhapnguyenlieu();
                n.setMaNhap(rs.getString("Ma_Nhap"));
                n.setMaNguyenlieu(rs.getString("Ma_Nguyenlieu"));
                n.setGia(rs.getDouble("Gia"));
                n.setTongTien(rs.getDouble("TongTien"));
                n.setNgayNhap(rs.getDate("Ngaynhap"));
                n.setSoLuong(rs.getDouble("Soluong"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo mã nhập
    public Nhapnguyenlieu selectById(String maNhap) {
        Nhapnguyenlieu n = null;
        String sql = "SELECT * FROM nhapnguyenlieu WHERE Ma_Nhap = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                n = new Nhapnguyenlieu();
                n.setMaNhap(rs.getString("Ma_Nhap"));
                n.setMaNguyenlieu(rs.getString("Ma_Nguyenlieu"));
                n.setGia(rs.getDouble("Gia"));
                n.setTongTien(rs.getDouble("TongTien"));
                n.setNgayNhap(rs.getDate("Ngaynhap"));
                n.setSoLuong(rs.getDouble("Soluong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }
 public List<Nhapnguyenlieu> selectByDate(java.util.Date date) {
    List<Nhapnguyenlieu> list = new ArrayList<>();
    String sql = "SELECT * FROM nhapnguyenlieu WHERE DATE(Ngaynhap) = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(date.getTime()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Nhapnguyenlieu n = new Nhapnguyenlieu();
            n.setMaNhap(rs.getString("Ma_Nhap"));
            n.setMaNguyenlieu(rs.getString("Ma_Nguyenlieu"));
            n.setGia(rs.getDouble("Gia"));
            n.setTongTien(rs.getDouble("TongTien"));
            n.setNgayNhap(rs.getDate("Ngaynhap"));
            n.setSoLuong(rs.getDouble("Soluong"));
            list.add(n);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
 public List<Nhapnguyenlieu> selectByMonthYear(int thang, int nam) {
    List<Nhapnguyenlieu> list = new ArrayList<>();
    String sql = "SELECT * FROM nhapnguyenlieu WHERE MONTH(Ngaynhap) = ? AND YEAR(Ngaynhap) = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Nhapnguyenlieu n = new Nhapnguyenlieu();
            n.setMaNhap(rs.getString("Ma_Nhap"));
            n.setMaNguyenlieu(rs.getString("Ma_Nguyenlieu"));
            n.setGia(rs.getDouble("Gia"));
            n.setTongTien(rs.getDouble("TongTien"));
            n.setNgayNhap(rs.getDate("Ngaynhap"));
            n.setSoLuong(rs.getDouble("Soluong"));
            list.add(n);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}
