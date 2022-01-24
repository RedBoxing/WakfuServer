package fr.redboxing.wakfu.server.utils;

import java.sql.*;

public class MySQLHelper {
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final String host;
    private final String database;
    private final String user;
    private final String pass;

    private Connection conn = null;
    private Statement stmt = null;

    public MySQLHelper(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = password;
    }

    public void open() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, pass);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet execute(String sql) {
        try {
            return stmt.executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void update(String sql) {
        try {
            stmt.executeUpdate(sql);
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }
}
