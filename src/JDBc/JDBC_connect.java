package JDBc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_connect {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3307/pbl3";
        String username = "root";
        String password = "Quoc2705@";
        Connection c = null;

        try {
            c = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối thành công đến MySQL!");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }

        return c; // KHÔNG được close() ở đây!
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}