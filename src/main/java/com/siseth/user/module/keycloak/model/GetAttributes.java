package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.internal.user.api.request.UserEmailReqDTO;
import com.siseth.user.module.internal.user.api.request.UserEmailWithTokenReqDTO;
import com.siseth.user.module.internal.user.api.request.UserIdReqDTO;
import com.siseth.user.module.keycloak.api.KeyCloakAttributesDTO;
import com.siseth.user.module.keycloak.api.KeyCloakUserInfoResDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GetAttributes extends HttpClientKeyCloak {

    private UserEmailReqDTO dto;
    private String accessToken;
    private String realm;
    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public Map<String,Object> getResponse() {

        URI uri = new URIBuilder(URL.replace("{{realm}}", realm))
                .addParameter("email", this.dto.getEmail())
                .addParameter("exact","true")
                .build();
        HttpResponse response = responseBuilder(new HttpGet(uri), this.accessToken);
        checkStatus(response);
        UserIdReqDTO[] userIds = new ObjectMapper().readValue(decode(response.getEntity()), UserIdReqDTO[].class);

        HttpResponse resp = responseBuilder(new HttpGet(URL.replace("{{realm}}", realm) + "/" + userIds[0].getId()), this.accessToken);

        JsonNode jsonNode = new ObjectMapper().readTree(decode(resp.getEntity()));
        checkStatus(resp);
        return new ObjectMapper().readValue(jsonNode.get("attributes").toString(), new TypeReference<Map<String,Object>>(){});
    }
}
