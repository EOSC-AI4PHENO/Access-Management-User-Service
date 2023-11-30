package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.internal.user.api.request.CreateUserReqDTO;
import com.siseth.user.module.internal.user.api.request.UserEmailWithTokenReqDTO;
import com.siseth.user.module.internal.user.api.request.UserUpdateReqDTO;
import com.siseth.user.module.internal.user.api.request.UserEmailReqDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.UUID;

@AllArgsConstructor
public class CreateUser extends HttpClientKeyCloak {

    private String accessToken;
    private CreateUserReqDTO dto;
    private String realm;

    public CreateUser(UserEmailReqDTO dto, String token, String realm) {
        this.dto = new CreateUserReqDTO(dto);
        this.accessToken = token;
        this.realm = realm;
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response = responseBuilder(new HttpPost(URL.replace("{{realm}}", realm)),
                new StringEntity(new ObjectMapper().writeValueAsString(this.dto)), this.accessToken);
        checkStatus(response);
        return "Successfully created user";
    }
}
