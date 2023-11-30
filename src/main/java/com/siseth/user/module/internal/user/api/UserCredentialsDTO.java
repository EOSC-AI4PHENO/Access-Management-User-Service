package com.siseth.user.module.internal.user.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDTO {
    private String type;
    private String value;
    private Boolean temporary;

    public UserCredentialsDTO(String password) {
        this.type = "password";
        this.value = password;
        this.temporary = false;
    }
}
