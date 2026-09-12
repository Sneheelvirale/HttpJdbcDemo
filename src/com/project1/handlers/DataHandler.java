package com.project1.handlers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project1.db.DBConnection;

public class DataHandler {

    public static String saveUser(String name, String email, String password) throws IOException {
        // ... unchanged, your existing insert logic stays as-is
        String query = "INSERT INTO users(name, email, password) VALUES(?,?,?)";
        if(name==null || name.length()<2) {
        	return "Name is too short";
        }
        if(email == null || !email.contains("@") ){
        	return "Email is not valid";
        }
        if(password==null || password.length()<=6 ) {
        	return "Password is too short";
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            int rowInserted = stmt.executeUpdate();
//            return rowInserted > 0;
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error";
        }
    }

    // NEW: reads all users back out of the DB.
    // Notice this returns a List<String> of pre-built JSON objects —
    // that's a shortcut for Project 1 only. In Project 3 (Spring Boot),
    // a proper JSON library (like Jackson) will do this serialization for
    // you automatically from a User object — you're doing it manually here
    // so you understand what's actually happening underneath.
    public static List<String> getAllUsers() throws IOException {
        List<String> users = new ArrayList<>();
        // NOTE: intentionally NOT selecting password — never expose
        // stored passwords back through an API response, even hashed ones,
        // unless there's a specific reason to.
        String query = "SELECT id, name, email FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                // Manually building a JSON string here. This is fragile
                // (breaks if name/email contain quotes) — a real project
                // would use a JSON library instead. Fine for learning purposes.
                String json = String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\"}",
                    id, name, email
                );
                users.add(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}