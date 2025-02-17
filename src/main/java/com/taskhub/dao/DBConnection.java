package com.taskhub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/taskhub?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Lucabrar49@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
