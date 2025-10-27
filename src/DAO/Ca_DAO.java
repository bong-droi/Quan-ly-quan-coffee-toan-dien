package DAO;

import JDBc.JDBC_connect;
import Lop.Ca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ca_DAO {
    private Connection conn = JDBC_connect.getConnection();

    

    public void insert(Ca ca) throws SQLException {
        String sql = "INSERT INTO ca (Ma_ca, Ngay, Time_start, Time_end, Luong_Ca, Soluong_ca, Soluong_dky) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ca.getMaCa());
        ps.setDate(2, ca.getNgay());
        ps.setTimestamp(3, new Timestamp(ca.getTimeStart().getTime()));
        ps.setTimestamp(4, new Timestamp(ca.getTimeEnd().getTime()));
        ps.setDouble(5, ca.getLuongCa());
        ps.setInt(6, ca.getSoluongCa());
        ps.setInt(7, ca.getSoluongDky());
        ps.executeUpdate();
        ps.close();
    }

    public boolean update(Ca ca) throws SQLException {
    String sql = "UPDATE ca SET Ngay=?, Time_start=?, Time_end=?, Luong_Ca=?, Soluong_ca=?,Soluong_dky=? WHERE Ma_ca=?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setDate(1, ca.getNgay());
    ps.setTimestamp(2,new Timestamp( ca.getTimeStart().getTime()));
    ps.setTimestamp(3, new Timestamp (ca.getTimeEnd().getTime()));
    ps.setDouble(4, ca.getLuongCa());
    ps.setInt(5, ca.getSoluongCa());
    ps.setInt(6,ca.getSoluongDky());
    ps.setString(7, ca.getMaCa());
    return ps.executeUpdate() > 0;

    }
    public void delete(String maCa) throws SQLException {
        String sql = "DELETE FROM ca WHERE Ma_ca = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maCa);
        ps.executeUpdate();
        ps.close();
    }

    public List<Ca> selectAll() throws SQLException {
        List<Ca> list = new ArrayList<>();
        String sql = "SELECT * FROM ca";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Ca ca = new Ca();
            ca.setMaCa(rs.getString("Ma_ca"));
            ca.setNgay(rs.getDate("Ngay"));
            ca.setTimeStart(rs.getTimestamp("Time_start"));
            ca.setTimeEnd(rs.getTimestamp("Time_end"));
            ca.setLuongCa(rs.getDouble("Luong_Ca"));
            ca.setSoluongCa(rs.getInt("Soluong_ca"));
            ca.setSoluongDky(rs.getInt("Soluong_dky"));
            list.add(ca);
        }
        rs.close();
        st.close();
        return list;
    }

    public Ca selectById(String maCa) throws SQLException {
        String sql = "SELECT * FROM ca WHERE Ma_ca = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maCa);
        ResultSet rs = ps.executeQuery();
        Ca ca = null;
        if (rs.next()) {
            ca = new Ca();
            ca.setMaCa(rs.getString("Ma_ca"));
            ca.setNgay(rs.getDate("Ngay"));
            ca.setTimeStart(rs.getTimestamp("Time_start"));
            ca.setTimeEnd(rs.getTimestamp("Time_end"));
            ca.setLuongCa(rs.getDouble("Luong_Ca"));
            ca.setSoluongCa(rs.getInt("Soluong_ca"));
            ca.setSoluongDky(rs.getInt("Soluong_dky"));
        }
        rs.close();
        ps.close();
        return ca;
    }
    public List<Ca> getByNgay(Date ngay) throws SQLException {
    String sql = "SELECT * FROM ca WHERE Ngay = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setDate(1, ngay);
    ResultSet rs = ps.executeQuery();

    List<Ca> list = new ArrayList<>();
    while (rs.next()) {
        Ca ca = new Ca();
            ca.setMaCa(rs.getString("Ma_ca"));
            ca.setNgay(rs.getDate("Ngay"));
            ca.setTimeStart(rs.getTimestamp("Time_start"));
            ca.setTimeEnd(rs.getTimestamp("Time_end"));
            ca.setLuongCa(rs.getDouble("Luong_Ca"));
            ca.setSoluongCa(rs.getInt("Soluong_ca"));
            ca.setSoluongDky(rs.getInt("Soluong_dky"));
        
        
        list.add(ca);
    }
    return list;
}
}
