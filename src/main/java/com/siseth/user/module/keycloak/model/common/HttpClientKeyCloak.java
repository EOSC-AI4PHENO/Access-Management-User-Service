package com.siseth.user.module.keycloak.model.common;

import com.siseth.user.module.internal.user.exception.*;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class HttpClientKeyCloak implements ClientKeyCloak  {

    @SneakyThrows
    protected HttpClient builder() {
        return HttpClients
                .custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    @SneakyThrows
    protected HttpResponse responseBuilder(HttpEntityEnclosingRequestBase request, StringEntity entity, String accessToken) {
        request.setEntity(entity);
        if(accessToken != null) {
            request = addAuthHeader(request, accessToken);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
        }
        return builder().execute(request);
    }

    @SneakyThrows
    protected HttpResponse responseBuilder(HttpGet request, String accessToken) {
        if(accessToken != null) {
            request = addAuthHeader(request, accessToken);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
        }
        return builder().execute(request);
    }

    @SneakyThrows
    protected HttpResponse responseBuilder(HttpDelete request, String accessToken) {
        if(accessToken != null) {
            request = addAuthHeader(request, accessToken);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
        }
        return builder().execute(request);
    }


    protected List<NameValuePair> getParams() {
        return new ArrayList<NameValuePair>();
    }

    @SneakyThrows
    protected String decode(HttpEntity entity) {
        return EntityUtils.toString(entity, "UTF-8");
    }

    protected HttpEntityEnclosingRequestBase addAuthHeader(HttpEntityEnclosingRequestBase http, String token) {
        http.addHeader("Authorization","Bearer " + token);
        return http;
    }

    protected HttpGet addAuthHeader(HttpGet http, String token) {
        http.addHeader("Authorization","Bearer " + token);
        return http;
    }

    protected HttpDelete addAuthHeader(HttpDelete http, String token) {
        http.addHeader("Authorization","Bearer " + token);
        return http;
    }

    @SneakyThrows
    protected void checkStatus(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode > 199 && statusCode < 300) // status 2xx - ok
            return;
//        Object obj = new ObjectMapper().readValue(decode(response.getEntity()), Object.class);
        String message = decode(response.getEntity());
        switch (statusCode){
            case 400:
                throw new BadRequestException(message);
            case 401:
                throw new UnauthorizedException("");
            case 403:
                throw new ForbiddenException("");
            case 404:
                throw new NotFoundException("");
            case 409:
                throw new ConflictException("Email already in use");
            case 500:
                throw new ServerException("Server error");
        }

    }

}
