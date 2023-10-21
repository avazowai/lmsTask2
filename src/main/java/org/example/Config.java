package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    private static final String url ="jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password= "1234";

    public static Connection getConnection(){
        try {
            System.out.println("Success");
            return DriverManager.getConnection(url,username,password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
};
