package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.internal.user.api.request.UserEmailReqDTO;
import com.siseth.user.module.internal.user.api.request.UserUpdateReqDTO;
import com.siseth.user.module.internal.user.api.request.UserWithIdUpdateReqDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

@AllArgsConstructor
public class UpdateUser extends HttpClientKeyCloak {

    private String accessToken;
    private UserUpdateReqDTO dto;
    private String id;
    private String realm;


    public UpdateUser(UserWithIdUpdateReqDTO dto, String token, String realm) {
        this.id = dto.getId();
        this.dto = new UserUpdateReqDTO(dto);
        this.accessToken = token;
        this.realm = realm;
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + id),
                new StringEntity(new ObjectMapper().writeValueAsString(this.dto)), this.accessToken);
        checkStatus(response);
        return "Successfully updated user";
    }
}
