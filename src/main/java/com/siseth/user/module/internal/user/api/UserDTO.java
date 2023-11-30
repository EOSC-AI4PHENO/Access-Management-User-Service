package com.siseth.user.module.internal.user.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String email;
    private String password;
    private String firstname;
    private String lastName;
    private int statusCode;
    private String status;
}