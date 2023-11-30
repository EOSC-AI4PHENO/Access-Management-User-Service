package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.keycloak.api.PreRegisterUsersDTO;
import com.siseth.user.module.keycloak.api.UserGroupsDTO;
import com.siseth.user.module.keycloak.api.UserIdDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class AllUsers extends HttpClientKeyCloak {

    private String accessToken;
    private String realm;

    private final String GROUP_LIST_URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public List<UserIdDTO> getResponse() {
        HttpResponse response = responseBuilder(new HttpGet(GROUP_LIST_URL.replace("{{realm}}", realm)), this.accessToken);
        checkStatus(response);

        return Arrays.stream(new ObjectMapper().readValue(decode(response.getEntity()), UserIdDTO[].class)).collect(Collectors.toList());
    }
}
