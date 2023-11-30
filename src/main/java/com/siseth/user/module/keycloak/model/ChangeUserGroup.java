package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.module.keycloak.api.PreRegisterUsersDTO;
import com.siseth.user.module.keycloak.api.UserGroupsDTO;
import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import javax.ws.rs.NotFoundException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserGroup extends HttpClientKeyCloak {

    private String accessToken;
    private String realm;
    private String userId;

    private String groupFrom;
    private String groupTo;

    private String groupToId;
    private String groupFromId;
    public ChangeUserGroup(String accessToken, String realm, String userId, String groupFrom, String groupTo) {
        this.accessToken = accessToken;
        this.realm = realm;
        this.groupFrom = groupFrom;
        this.groupTo = groupTo;
        this.groupFromId = "";
        this.groupToId = "";
        this.userId = userId;
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "groups/{{groupFrom}}/members";
    private final String GROUP_LIST_URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "groups";

    private final String DELETE_FROM_GROUP_URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users/{{user_id}}/groups/{{group_id}}";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse group_response = responseBuilder(new HttpGet(GROUP_LIST_URL.replace("{{realm}}", realm)), this.accessToken);
        checkStatus(group_response);

        List<UserGroupsDTO> group_list =
                new ObjectMapper().readValue(
                        decode(group_response.getEntity()),
                        new TypeReference<List<UserGroupsDTO>>() { });


        group_list.get(0).getSubGroups().forEach(x -> {
            if(x.getName().equals(groupFrom)) {
                groupFromId = x.getId();
            } else if (x.getName().equals(groupTo)) {
                groupToId = x.getId();
            }
        });
        if (groupToId.equals("") || groupFromId.equals("") )
            throw new NotFoundException("Group not found");

        HttpResponse deleteRes = responseBuilder(
                new HttpDelete(DELETE_FROM_GROUP_URL
                        .replace("{{realm}}", realm)
                        .replace("{{user_id}}", userId)
                        .replace("{{group_id}}", groupFromId)
                ), this.accessToken);

        checkStatus(deleteRes);

        HttpResponse addRes = responseBuilder(
                new HttpPut(DELETE_FROM_GROUP_URL
                        .replace("{{realm}}", realm)
                        .replace("{{user_id}}", userId)
                        .replace("{{group_id}}", groupToId)
                ), new StringEntity(""), this.accessToken);

        checkStatus(addRes);

        return "ok";

    }
}
