package DAO;
import JDBc.JDBC_connect;
import Lop.LichLamViec;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichLamViec_DAO {
    private Connection conn = JDBC_connect.getConnection();;

    

    // Thêm mới
    public boolean insert(LichLamViec lich) throws SQLException {
        String sql = "INSERT INTO chia_lich_lam_viec (Ma_phanlich, Ma_Nhanvien, Ngay, Ca, Luong, Trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, lich.getMaPhanLich());
        ps.setString(2, lich.getMaNhanVien());
        ps.setDate(3, lich.getNgay());
        ps.setString(4, lich.getCa());
        ps.setDouble(5, lich.getLuong());
        ps.setInt(6, lich.getTrangThai());
        return ps.executeUpdate() > 0;
    }

    // Lấy theo mã
    public LichLamViec getById(String maPhanLich) throws SQLException {
        String sql = "SELECT * FROM chia_lich_lam_viec WHERE Ma_phanlich = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maPhanLich);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return extractFromResultSet(rs);
        }
        return null;
    }

    // Cập nhật
    public boolean update(LichLamViec lich) throws SQLException {
        String sql = "UPDATE chia_lich_lam_viec SET Ma_Nhanvien=?, Ngay=?, Ca=?, Luong=?, Trang_thai=? WHERE Ma_phanlich=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, lich.getMaNhanVien());
        ps.setDate(2, lich.getNgay());
        ps.setString(3, lich.getCa());
        ps.setDouble(4, lich.getLuong());
        ps.setInt(5, lich.getTrangThai());
        ps.setString(6, lich.getMaPhanLich());
        return ps.executeUpdate() > 0;
    }

    // Xoá
    public boolean delete(String maPhanLich) throws SQLException {
        String sql = "DELETE FROM chia_lich_lam_viec WHERE Ma_phanlich = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maPhanLich);
        return ps.executeUpdate() > 0;
    }

    // Lấy tất cả
    public List<LichLamViec> getAll() throws SQLException {
        List<LichLamViec> list = new ArrayList<>();
        String sql = "SELECT * FROM chia_lich_lam_viec";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            list.add(extractFromResultSet(rs));
        }
        return list;
    }

    private LichLamViec extractFromResultSet(ResultSet rs) throws SQLException {
        LichLamViec lich = new LichLamViec();
        lich.setMaPhanLich(rs.getString("Ma_phanlich"));
        lich.setMaNhanVien(rs.getString("Ma_Nhanvien"));
        lich.setNgay(rs.getDate("Ngay"));
        lich.setCa(rs.getString("Ca"));
        lich.setLuong(rs.getDouble("Luong"));
        lich.setTrangThai(rs.getInt("Trang_thai"));
        return lich;
    }
    public List<LichLamViec> getByCa(String maCa) throws SQLException {
    List<LichLamViec> list = new ArrayList<>();
    String sql = "SELECT * FROM chia_lich_lam_viec WHERE Ca = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, maCa);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        list.add(extractFromResultSet(rs));
    }
    return list;
}
    public List<LichLamViec> selectByMaNhanVien(String maNV) throws SQLException {
    List<LichLamViec> list = new ArrayList<>();
    String sql = "SELECT * FROM chia_lich_lam_viec WHERE Ma_Nhanvien = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, maNV);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        list.add(extractFromResultSet(rs));
    }
    return list;
}
    public List<LichLamViec> selectByThang(int thang, int nam) throws SQLException {
    List<LichLamViec> list = new ArrayList<>();
    String sql = "SELECT * FROM chia_lich_lam_viec WHERE MONTH(Ngay) = ? AND YEAR(Ngay) = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, thang);
    ps.setInt(2, nam);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        list.add(extractFromResultSet(rs));
    }
    return list;
}

}
