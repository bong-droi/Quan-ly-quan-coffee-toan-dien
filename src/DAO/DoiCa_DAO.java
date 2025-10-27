package DAO;

import JDBc.JDBC_connect;
import Lop.DoiCa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoiCa_DAO {
    private final Connection conn = JDBC_connect.getConnection();

    // Thêm một đổi ca mới
    public boolean insert(DoiCa dc) throws SQLException {
        String sql = "INSERT INTO doi_ca (Ma_doica, Ma_Phanlich1, Ma_Phanlich2, Ma_NV1, Ma_NV2, `Trang_thai`) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dc.getMaDoiCa());
            ps.setString(2, dc.getMaPhanLich1());
            ps.setString(3, dc.getMaPhanLich2());
            ps.setString(4, dc.getMaNV1());
            ps.setString(5, dc.getMaNV2());
            ps.setInt(6, dc.getTrangThai());
            ps.executeUpdate();
        }catch (Exception e) {
        e.printStackTrace();
        return false;
    }
        return true;
    }

    // Cập nhật thông tin đổi ca
    public void update(DoiCa dc) throws SQLException {
        String sql = "UPDATE doi_ca SET Ma_Phanlich1 = ?, Ma_Phanlich2 = ?, Ma_NV1 = ?, Ma_NV2 = ?, `Trang_thai` = ? "
                   + "WHERE Ma_doica = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dc.getMaPhanLich1());
            ps.setString(2, dc.getMaPhanLich2());
            ps.setString(3, dc.getMaNV1());
            ps.setString(4, dc.getMaNV2());
            ps.setInt(5, dc.getTrangThai());
            ps.setString(6, dc.getMaDoiCa());
            ps.executeUpdate();
        }
    }

    // Xóa một đổi ca theo mã
    public void delete(String maDoiCa) throws SQLException {
        String sql = "DELETE FROM doi_ca WHERE Ma_doica = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDoiCa);
            ps.executeUpdate();
        }
    }

    // Lấy toàn bộ danh sách đổi ca
    public List<DoiCa> selectAll() throws SQLException {
        List<DoiCa> list = new ArrayList<>();
        String sql = "SELECT * FROM doi_ca";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DoiCa dc = new DoiCa(
                    rs.getString("Ma_doica"),
                    rs.getString("Ma_Phanlich1"),
                    rs.getString("Ma_Phanlich2"),
                    rs.getString("Ma_NV1"),
                    rs.getString("Ma_NV2"),
                    rs.getInt("Trang_thai")
                );
                list.add(dc);
            }
        }
        return list;
    }

    // Tìm một đổi ca theo mã
    public DoiCa selectById(String maDoiCa) throws SQLException {
        String sql = "SELECT * FROM doi_ca WHERE Ma_doica = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDoiCa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DoiCa(
                        rs.getString("Ma_doica"),
                        rs.getString("Ma_Phanlich1"),
                        rs.getString("Ma_Phanlich2"),
                        rs.getString("Ma_NV1"),
                        rs.getString("Ma_NV2"),
                        rs.getInt("Trang_thai")
                    );
                }
            }
        }
        return null;
    }
    // Lấy danh sách đổi ca theo Ma_NV1
public List<DoiCa> selectByMaNV1(String maNV1) throws SQLException {
    List<DoiCa> list = new ArrayList<>();
    String sql = "SELECT * FROM doi_ca WHERE Ma_NV1 = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maNV1);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DoiCa dc = new DoiCa(
                    rs.getString("Ma_doica"),
                    rs.getString("Ma_Phanlich1"),
                    rs.getString("Ma_Phanlich2"),
                    rs.getString("Ma_NV1"),
                    rs.getString("Ma_NV2"),
                    rs.getInt("Trang_thai")
                );
                list.add(dc);
            }
        }
    }
    return list;
}
public void deleteIfWaitingByPhanLich(String maPhanLich) throws SQLException {
    String sql = "DELETE FROM doi_ca WHERE (Ma_Phanlich1 = ? OR Ma_Phanlich2 = ?) AND Trang_thai = 0";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maPhanLich);
        ps.setString(2, maPhanLich);
        ps.executeUpdate();
    }
}

}
