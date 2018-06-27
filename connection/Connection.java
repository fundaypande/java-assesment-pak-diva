package connection;

import javax.swing.*;
import java.sql.*;


public class Connection {

    public java.sql.Connection conn;
    private Statement statement;
    public static Connection db;
    private PreparedStatement preparedStatement;

    private Connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
        } catch (Exception sqle) {
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tidak Bisa Mengakses ke Database");
            System.exit(0);
        }
    }

    //singletonPatern
    public static synchronized Connection getDbCon() {
        if ( db == null ) {
            db = new Connection();
        }
        return db;
    }

    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }

    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;

    }
}
