package com.example.elasticapi.service;

import com.example.elasticapi.model.MyUserDetails;
import com.example.elasticapi.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=getUserByUsername(username);
        if (user==null){
            System.out.println("usernot found");
            throw new UsernameNotFoundException("user not found");
        }
        return new MyUserDetails(user);
    }


    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        User user = null;

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/db/user.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String dbUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    user = new User(id, dbUsername, password);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
