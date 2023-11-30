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
import org.apache.http.client.methods.HttpGet;

import javax.ws.rs.NotFoundException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class GetUsersFromGroup extends HttpClientKeyCloak {

    private String accessToken;
    private String realm;
    private String group;

    private String preReg;

    public GetUsersFromGroup(String accessToken, String realm, String group) {
        this.accessToken = accessToken;
        this.realm = realm;
        this.group = group;
        this.preReg = "";
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "groups/{{preReg}}/members";
    private final String GROUP_LIST_URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "groups";

    @SneakyThrows
    @Override
    public List<PreRegisterUsersDTO> getResponse() {
        HttpResponse group_response = responseBuilder(new HttpGet(GROUP_LIST_URL.replace("{{realm}}", realm)), this.accessToken);
        checkStatus(group_response);

        List<UserGroupsDTO> group_list =
                new ObjectMapper().readValue(
                        decode(group_response.getEntity()),
                        new TypeReference<List<UserGroupsDTO>>() { });


        group_list.get(0).getSubGroups().forEach(x -> {
            if(x.getName().equals(this.group)) {
                preReg = x.getId();
            }
        });
        if (preReg.equals(""))
            throw new NotFoundException("Group not found");


        HttpResponse response = responseBuilder(new HttpGet(URL.replace("{{realm}}", realm).replace("{{preReg}}", this.preReg)), this.accessToken);
        checkStatus(response);

        List<PreRegisterUsersDTO> result =
        new ObjectMapper().readValue(
                                    decode(response.getEntity()),
                new TypeReference<List<PreRegisterUsersDTO>>() { });
        return result;
    }
}
