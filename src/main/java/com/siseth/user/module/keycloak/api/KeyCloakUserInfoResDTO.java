package com.siseth.user.module.keycloak.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakUserInfoResDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}

