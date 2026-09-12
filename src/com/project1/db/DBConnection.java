package com.project1.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() throws IOException, SQLException {
    	System.out.println("getConnection called");
    	FileInputStream file = null;
    	Properties prop = new Properties();
    	try {
    	    file = new FileInputStream("config.properties");
    	    prop.load(file);
    	    System.out.println("Properties loaded successfully");
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        }

        return DriverManager.getConnection(prop.getProperty("db.url"),prop.getProperty("db.user"),prop.getProperty("db.password"));
    }
}