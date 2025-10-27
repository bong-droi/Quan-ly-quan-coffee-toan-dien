package DAO;


import java.sql.*;
import java.util.*;
import Lop.Ban;
import JDBc.JDBC_connect;

public class Ban_DAO {

    public void insert(Ban ban) {
        String sql = "INSERT INTO ban (Maban, Trangthai) VALUES (?, ?)";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ban.getMaban());
            ps.setString(2, ban.getTrangthai());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Ban ban) {
        String sql = "UPDATE ban SET Trangthai = ? WHERE Maban = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ban.getTrangthai());
            ps.setInt(2, ban.getMaban());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int maban) {
        String sql = "DELETE FROM ban WHERE Maban = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maban);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ban> selectAll() {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM ban";
        try (Connection con = JDBC_connect.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Ban ban = new Ban();
                ban.setMaban(rs.getInt("Maban"));
                ban.setTrangthai(rs.getString("Trangthai"));
                list.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Ban selectById(int maban) {
        Ban ban = null;
        String sql = "SELECT * FROM ban WHERE Maban = ?";
        try (Connection con = JDBC_connect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maban);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ban = new Ban();
                ban.setMaban(rs.getInt("Maban"));
                ban.setTrangthai(rs.getString("Trangthai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ban;
    }
}
