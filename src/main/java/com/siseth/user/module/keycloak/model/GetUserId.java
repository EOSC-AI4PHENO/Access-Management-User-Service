package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.internal.user.api.request.UserEmailReqDTO;
import com.siseth.user.module.internal.user.api.request.UserEmailWithTokenReqDTO;
import com.siseth.user.module.internal.user.api.request.UserIdReqDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GetUserId extends HttpClientKeyCloak {

    private String accessToken;
    private String email;
    private String realm;

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        URI uri = new URIBuilder(URL.replace("{{realm}}", realm))
                .addParameter("email", this.email)
                .addParameter("exact","true")
                .build();
        HttpResponse response = responseBuilder(new HttpGet(uri), this.accessToken);
        checkStatus(response);

        UserIdReqDTO[] userIds = new ObjectMapper().readValue(decode(response.getEntity()), UserIdReqDTO[].class);

        return userIds[0].getId();
    }
}
