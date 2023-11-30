package com.siseth.user.module.keycloak.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakUserInfoDTO {
    private String sub;
    private Boolean email_verified;
    private String gender;
    private String name;
    private String preferred_username;
    private String given_name;
    private String family_name;
}

