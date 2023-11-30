package com.siseth.user.module.keycloak.model;

import com.siseth.user.module.keycloak.constant.KeyCloakConstant;
import com.siseth.user.module.keycloak.model.common.HttpClientKeyCloak;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

@AllArgsConstructor
public class EnableUser extends HttpClientKeyCloak {

    private String id;
    private String accessToken;
    private Boolean accept;
    private String realm;
    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + id),
                new StringEntity("{\"enabled\":\""+accept.toString()+"\"}"), this.accessToken);
        checkStatus(response);
        if (accept)
            return "Successfully enabled user of id: " + id;

        return "Successfully disabled user of id: " + id;
    }
}
