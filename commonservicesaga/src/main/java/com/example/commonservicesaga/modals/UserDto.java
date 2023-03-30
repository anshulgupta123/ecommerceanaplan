package com.example.commonservicesaga.modals;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;

    private String password;
}

