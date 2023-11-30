package com.siseth.user.module.keycloak.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakSignInDTO {
    private String refresh_token;
    private String access_token;
}
