package com.siseth.user.module.keycloak.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.siseth.user.module.internal.user.api.UserCredentialsDTO;
import com.siseth.user.module.internal.user.api.request.*;
import com.siseth.user.module.internal.user.exception.ForbiddenException;
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
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ChangePassword extends HttpClientKeyCloak {

    private String id;
    private PasswordChangeUserReqDTO dto;
    private PasswordChangeUserWithGroupReqDTO dtoWithGroup;
    private String accessToken;
    private String newUserToken;
    private String email;
    private String realm;

    public ChangePassword(ChangePasswordReqDTO dto, String accessToken, String newUserToken, String realm) {
        this.accessToken = accessToken;
        this.newUserToken = newUserToken;
        this.id = "";
        this.email = dto.getEmail();
        this.dtoWithGroup = new PasswordChangeUserWithGroupReqDTO(new UserCredentialsDTO(dto.getPassword()));
        this.realm = realm;
    }

    public ChangePassword(ChangePasswordWithIdReqDTO dto, String accessToken, String realm) {
        this.accessToken = accessToken;
        this.newUserToken = "";
        this.id = "";
        this.email = dto.getEmail();
        this.dto = new PasswordChangeUserReqDTO(new UserCredentialsDTO(dto.getNewPassword()));
        this.realm = realm;
    }

    private final String URL = KeyCloakConstant.KEYCLOAK_ADMIN_URL + "users";

    @SneakyThrows
    @Override
    public String getResponse() {
        HttpResponse response;
        if (this.newUserToken.equals("")) {
            this.id = validUser(buildUriGetUser());
            response = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + this.id),
                    new StringEntity(new ObjectMapper().writeValueAsString(this.dto)), this.accessToken);
        }
        else {
            this.id = validUserWithToken(buildUriGetUserWithToken());
            response = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + this.id),
                    new StringEntity(new ObjectMapper().writeValueAsString(this.dtoWithGroup)), this.accessToken);

            Map<String, Object> att = new HashMap<>();
            att.put("termsOfUseAccepted", true);
            att.put("privacyPolicyAccepted", true);

            Map<String, Map> entity = new HashMap<>();

            entity.put("attributes", att);

            HttpResponse resp = responseBuilder(new HttpPut(URL.replace("{{realm}}", realm) + "/" + this.id),
                    new StringEntity(new ObjectMapper().writeValueAsString(entity)), this.accessToken);

        }


        checkStatus(response);
        return id;
    }
    @SneakyThrows
    public String validUserWithToken(URI uri) {
        HttpResponse response = responseBuilder(new HttpGet(uri), this.accessToken);

        String responseString = new String(ByteStreams.toByteArray(response.getEntity().getContent()), Charsets.UTF_8);
        JsonNode rootNode =  new ObjectMapper().readTree(responseString);
        String tokenUrl = rootNode.get(0).get("attributes").get("newUserToken").get(0).toString();
        String tokenExpireTime = rootNode.get(0).get("attributes").get("tokenExpireTime").get(0).toString();
        OffsetDateTime tokenTime = OffsetDateTime.parse(tokenExpireTime.replace("\"",""));
        Long duration = Duration.between(tokenTime,OffsetDateTime.now()).toHours();
        checkStatus(response);
        if (duration > 0 || !tokenUrl.replace("\"","").equals(this.newUserToken))
            throw new ForbiddenException("the user does not have a valid token");

       return rootNode.get(0).get("id").toString().replace("\"","");
    }

    @SneakyThrows
    public String validUser(URI uri) {
        HttpResponse response = responseBuilder(new HttpGet(uri), this.accessToken);
        String responseString = new String(ByteStreams.toByteArray(response.getEntity().getContent()), Charsets.UTF_8);
        JsonNode rootNode =  new ObjectMapper().readTree(responseString);
        checkStatus(response);

        return rootNode.get(0).get("id").toString().replace("\"","");
    }

    @SneakyThrows
    private URI buildUriGetUser() {
        return new URIBuilder(this.URL.replace("{{realm}}", realm))
                .addParameter("email", this.email)
                .addParameter("exact","true")
                .build();
    }

    @SneakyThrows
    private URI buildUriGetUserWithToken() {
        return new URIBuilder(this.URL.replace("{{realm}}", realm))
                .addParameter("q","newUserToken:"+newUserToken)
                .addParameter("email", this.email)
                .addParameter("exact","true")
                .build();
    }
}
