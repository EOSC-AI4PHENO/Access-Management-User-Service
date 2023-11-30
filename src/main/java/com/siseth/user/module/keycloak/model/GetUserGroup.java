package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.keycloak.api.GroupNameDTO;
import com.siseth.user.module.keycloak.api.UserIdDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class GetUserGroup extends HttpClientKeyCloak {

    private String accessToken;
    private String realm;
    private String userId;

    private final String GROUP_LIST_URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users/{{userId}}/groups";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response = responseBuilder(new HttpGet(GROUP_LIST_URL.replace("{{realm}}", realm).replace("{{userId}}", userId)), this.accessToken);
        checkStatus(response);

        return new ObjectMapper().readValue(decode(response.getEntity()), GroupNameDTO[].class)[0].getName();
    }
}
