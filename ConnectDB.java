import java.sql.*;

/**
 * Write a description of class ConnectDB here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ConnectDB {
    Connection conn;
    Statement stmt;

    public static void main(String[] args) throws Exception {
        try {
            ConnectDB conn = new ConnectDB();
            if (conn.stmt != null) {
                System.out.println("connection successfull");
            }
        } catch (SQLException e) {
            System.out.println("connection failed");
        }
    }

    public ConnectDB() throws Exception {
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection("jdbc:sqlite:./sekolah.db");
        this.stmt = this.conn.createStatement();
    }
}
