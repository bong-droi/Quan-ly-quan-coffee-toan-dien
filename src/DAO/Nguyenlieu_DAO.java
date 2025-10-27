/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Lop.Nguyenlieu;
import JDBc.JDBC_connect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMINa
 */
public class Nguyenlieu_DAO {
     // Thêm nguyên liệu
    public void insert(Nguyenlieu nl) {
        String sql = "INSERT INTO nguyenlieu (Ma_Nglieu, Ten_Nglieu, Soluong_NL, Gia, Soluong_NLin, Date_Nglieu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nl.getMa_Nglieu());
            ps.setString(2, nl.getTen_Nglieu());
            ps.setDouble(3, nl.getSL_NL());
            ps.setDouble(4, nl.getGia_Nglieu());
            ps.setInt(5, nl.getSL_NLin());
            ps.setDate(6, new java.sql.Date(nl.getDate_Nglieu().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật nguyên liệu
    public void update(Nguyenlieu nl) {
        String sql = "UPDATE nguyenlieu SET Ten_Nglieu = ?, Soluong_NL = ?, Gia = ?, Soluong_NLin = ?, Date_Nglieu = ? WHERE Ma_Nglieu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nl.getTen_Nglieu());
            ps.setDouble(2, nl.getSL_NL());
            ps.setDouble(3, nl.getGia_Nglieu());
            ps.setInt(4, nl.getSL_NLin());
            ps.setDate(5, new java.sql.Date(nl.getDate_Nglieu().getTime()));
            ps.setString(6, nl.getMa_Nglieu());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa nguyên liệu
    public void delete(String maNglieu) {
        String sql = "DELETE FROM nguyenlieu WHERE Ma_Nglieu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNglieu);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ danh sách nguyên liệu
    public List<Nguyenlieu> selectAll() {
        List<Nguyenlieu> list = new ArrayList<>();
        String sql = "SELECT * FROM nguyenlieu";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Nguyenlieu nl = new Nguyenlieu();
                nl.setMa_Nglieu(rs.getString("Ma_Nglieu"));
                nl.setTen_Nglieu(rs.getString("Ten_Nglieu"));
                nl.setSL_NL(rs.getInt("Soluong_NL"));
                nl.setGia_Nglieu(rs.getDouble("Gia"));
                nl.setSL_NLin(rs.getInt("Soluong_NLin"));
                nl.setDate_Nglieu(rs.getDate("Date_Nglieu"));
                list.add(nl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm nguyên liệu theo mã
    public Nguyenlieu selectById(String maNglieu) {
        Nguyenlieu nl = null;
        String sql = "SELECT * FROM nguyenlieu WHERE Ma_Nglieu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNglieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nl = new Nguyenlieu();
                nl.setMa_Nglieu(rs.getString("Ma_Nglieu"));
                nl.setTen_Nglieu(rs.getString("Ten_Nglieu"));
                nl.setSL_NL(rs.getInt("Soluong_NL"));
                nl.setGia_Nglieu(rs.getDouble("Gia"));
                nl.setSL_NLin(rs.getInt("Soluong_NLin"));
                nl.setDate_Nglieu(rs.getDate("Date_Nglieu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nl;
    }
    public double getGiaByMaNguyenlieu(String ma) {
    String sql = "SELECT Gia FROM nguyenlieu WHERE Ma_Nglieu = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, ma);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("Gia");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}

public void capNhatSoLuong(String ma, double soLuongThem) {
    String sql = "UPDATE nguyenlieu SET Soluong_NL = Soluong_NL + ? WHERE Ma_Nglieu = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDouble(1, soLuongThem);
        ps.setString(2, ma);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public int getSoLuongTon(String maNgLieu) {
    int sl = 0;
    String sql = "SELECT Soluong_NL FROM nguyenlieu WHERE Ma_Nglieu = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maNgLieu);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            sl = rs.getInt("Soluong_NL");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sl;
}

    
}
