package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.internal.user.api.TokenDTO;
import com.siseth.user.module.internal.user.api.request.*;
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
public class ResetPassword extends HttpClientKeyCloak {

    private String accessToken;
    private UserEmailWithTokenReqDTO dto;
    private String generatedToken;
    private Map<String, Object> att;
    private String realm;

    public ResetPassword(UserEmailReqDTO dto, String token, Map<String, Object> att, String realm) {
        this.att = att;
        this.generatedToken = UUID.randomUUID().toString();
        this.dto = new UserEmailWithTokenReqDTO(dto, this.generatedToken);
        this.accessToken = token;
        this.realm = realm;
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        URI uri = new URIBuilder(URL.replace("{{realm}}", realm))
                .addParameter("email", this.dto.getEmail())
                .addParameter("exact","true")
                .build();
        HttpResponse response = responseBuilder(new HttpGet(uri), this.accessToken);
        checkStatus(response);

        UserIdReqDTO[] userIds = new ObjectMapper().readValue(decode(response.getEntity()), UserIdReqDTO[].class);

        this.att.put("newUserToken", dto.getNewUserToken());
        this.att.put("tokenExpireTime", dto.gettokenExpireTime());

        Map<String, Map> entity = new HashMap<>();

        entity.put("attributes", this.att);

        HttpResponse resp = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + userIds[0].getId()),
                new StringEntity(new ObjectMapper().writeValueAsString(entity)), this.accessToken);
        checkStatus(resp);
        return this.generatedToken;
    }
}
