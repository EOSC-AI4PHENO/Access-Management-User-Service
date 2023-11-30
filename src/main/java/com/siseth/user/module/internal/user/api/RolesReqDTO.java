package com.siseth.user.module.internal.user.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolesReqDTO {
    private List<String> roles;

    public Boolean isAdmin() {
        return roles.contains("Admin");
    }

    public Boolean isPowerUser() {
        return roles.contains("Power User");
    }
}