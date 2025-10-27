/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import JDBc.JDBC_connect;
import Lop.LoaiMon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class LoaiMon_DAO {
     public void insert(LoaiMon loaiMon) {
        String sql = "INSERT INTO loaimon (Ma_LM, Ten_LM) VALUES (?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getMa_LM());
            ps.setString(2, loaiMon.getTen_LM());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật loại món
    public void update(LoaiMon loaiMon) {
        String sql = "UPDATE loaimon SET Ten_LM = ? WHERE Ma_LM = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon.getTen_LM());
            ps.setString(2, loaiMon.getMa_LM());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa loại món
    public void delete(String maLM) {
        String sql = "DELETE FROM loaimon WHERE Ma_LM = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLM);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<LoaiMon> selectByName(String keyword) {
    List<LoaiMon> list = new ArrayList<>();
    String sql = "SELECT * FROM loaimon WHERE Ten_LM LIKE ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LoaiMon loaiMon = new LoaiMon();
            loaiMon.setMa_LM(rs.getString("Ma_LM"));
            loaiMon.setTen_LM(rs.getString("Ten_LM"));
            list.add(loaiMon);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    // Lấy danh sách tất cả loại món
    public List<LoaiMon> selectAll() {
        List<LoaiMon> list = new ArrayList<>();
        String sql = "SELECT * FROM loaimon";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LoaiMon loaiMon = new LoaiMon();
                loaiMon.setMa_LM(rs.getString("Ma_LM"));
                loaiMon.setTen_LM(rs.getString("Ten_LM"));
                list.add(loaiMon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm loại món theo mã
    public LoaiMon selectById(String maLM) {
        LoaiMon loaiMon = null;
        String sql = "SELECT * FROM loaimon WHERE Ma_LM = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loaiMon = new LoaiMon();
                loaiMon.setMa_LM(rs.getString("Ma_LM"));
                loaiMon.setTen_LM(rs.getString("Ten_LM"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiMon;
    }
    public boolean existsById(String maLM) {
    String sql = "SELECT Ma_LM FROM loaimon WHERE Ma_LM = ?";
    try (Connection con = JDBC_connect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, maLM);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // nếu có dữ liệu => tồn tại
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
}
