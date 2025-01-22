package com.example.elasticapi.service;

import com.example.elasticapi.model.MyUserDetails;
import com.example.elasticapi.model.Roles;
import com.example.elasticapi.model.User;

import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        String sql = "SELECT id, username, password ,role FROM users WHERE username = ?";
        User user = null;

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/db/user.db");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String dbUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String rolesString=resultSet.getString("role");
                    List<Roles> roles = Arrays.stream(rolesString.split(","))
                            .map(String::trim) // Trim whitespace if needed
                            .map(Roles::valueOf) // Convert each string to the Role enum
                            .collect(Collectors.toList());
                    user = new User(id, dbUsername, password,roles);
                    System.out.println(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
