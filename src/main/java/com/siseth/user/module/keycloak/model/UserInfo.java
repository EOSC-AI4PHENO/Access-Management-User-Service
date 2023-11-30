package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.keycloak.api.KeyCloakUserInfoResDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

@AllArgsConstructor
public class UserInfo extends HttpClientKeyCloak {

    private String id;
    private String accessToken;
    private String realm;
    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public KeyCloakUserInfoResDTO getResponse() {
        HttpResponse response = responseBuilder(new HttpGet(URL.replace("{{realm}}", realm) + "/" + id), this.accessToken);
        KeyCloakUserInfoResDTO dto = new ObjectMapper().readValue(decode(response.getEntity()), KeyCloakUserInfoResDTO.class);
        checkStatus(response);
        return dto;
    }
}
