package com.siseth.user.module.keycloak.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreRegisterUsersDTO {

    private String id;
    private String email;
    private Att attributes;
    private Boolean enabled;

    public Boolean tokenExpired() {
        return attributes.tokenExpired();
    }

    public String getToken(){
        return attributes.getNewUserToken().get(0);
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Att {
    private List<String> newUserToken;
    private List<String> tokenExpireTime;


    public Boolean tokenExpired() {
        Long duration = Duration.between(OffsetDateTime.parse(tokenExpireTime.get(0)), OffsetDateTime.now()).toHours();

        return duration > 24;
    }
}
