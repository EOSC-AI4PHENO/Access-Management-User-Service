package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.keycloak.api.PreRegisterUsersDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DeleteUser extends HttpClientKeyCloak {

    private String accessToken;
    private String userId;
    private String realm;
    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users/";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response = responseBuilder(new HttpDelete(URL.replace("{{realm}}", realm) + userId), this.accessToken);
        checkStatus(response);

        return "Deleted user of id: " + this.userId;
    }
}
