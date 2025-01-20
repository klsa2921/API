package com.example.elasticapi.model;

import lombok.Data;

@Data
public class User {
    private String userid;
    private Enum<Roles> role;
}
