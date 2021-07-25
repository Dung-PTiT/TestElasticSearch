/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dell
 */
public class JDBCConnection {

    private final String HOST = "localhost";
    private final String USERNAME = "root";
    private final String PASSWORD = "12345";
    private final String DATABASE = "newlifesystem";
    private final String PORT = "3306";
    private final String CLASSNAME = "com.mysql.cj.jdbc.Driver";
    private final String URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE);

    private Connection connection = null;

    private JDBCConnection() {
        try {
            Class.forName(CLASSNAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static JDBCConnection getInstance() {
        return JDBCConnectionProviderHolder.INSTANCE;
    }

    private static class JDBCConnectionProviderHolder {

        private static final JDBCConnection INSTANCE = new JDBCConnection();
    }

    public static List<String> getTables() throws SQLException {
        Connection connection = JDBCConnection.getInstance().getConnection();
        List<String> tables = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("Show tables");
        while (rs.next()) {
            tables.add(rs.getString(1));
            System.out.println(rs.getString(1));
        }
        return tables;
    }
    
    public static void main(String[] args) throws SQLException {
       getTables().toArray();
    }
}
