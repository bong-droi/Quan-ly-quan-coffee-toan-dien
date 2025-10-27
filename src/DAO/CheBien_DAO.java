/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import JDBc.JDBC_connect;
import Lop.CheBien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class CheBien_DAO {
     // Thêm mới một nguyên liệu chế biến cho món ăn
    public void insert(CheBien cb) {
        String sql = "INSERT INTO chebien (Ma_Mon, Ma_Nglieu, Soluong_chebien) VALUES (?, ?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cb.getMa_Mon());
            ps.setString(2, cb.getMa_Nglieu());
            ps.setDouble(3, cb.getSL_chebien());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật số lượng chế biến
    public void update(CheBien cb) {
        String sql = "UPDATE chebien SET Soluong_chebien = ? WHERE Ma_Mon = ? AND Ma_Nglieu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cb.getSL_chebien());
            ps.setString(2, cb.getMa_Mon());
            ps.setString(3, cb.getMa_Nglieu());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa nguyên liệu ra khỏi công thức chế biến của món
    public void delete(String maMon, String maNgLieu) {
        String sql = "DELETE FROM chebien WHERE Ma_Mon = ? AND Ma_Nglieu = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ps.setString(2, maNgLieu);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ công thức chế biến
    public List<CheBien> selectAll() {
        List<CheBien> list = new ArrayList<>();
        String sql = "SELECT * FROM chebien";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                CheBien cb = new CheBien();
                cb.setMa_Mon(rs.getString("Ma_Mon"));
                cb.setMa_Nglieu(rs.getString("Ma_Nglieu"));
                cb.setSL_chebien(rs.getInt("Soluong_chebien"));
                list.add(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách nguyên liệu cho 1 món ăn
    public List<CheBien> selectByMaMon(String maMon) {
        List<CheBien> list = new ArrayList<>();
        String sql = "SELECT * FROM chebien WHERE Ma_Mon = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CheBien cb = new CheBien();
                cb.setMa_Mon(rs.getString("Ma_Mon"));
                cb.setMa_Nglieu(rs.getString("Ma_Nglieu"));
                cb.setSL_chebien(rs.getInt("Soluong_chebien"));
                list.add(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
