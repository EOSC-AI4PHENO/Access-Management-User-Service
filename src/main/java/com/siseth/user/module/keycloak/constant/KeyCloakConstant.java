package com.siseth.user.module.keycloak.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakConstant {
    public static String KEYCLOAK_URL;

    public static String KEYCLOAK_ADMIN_URL;



    @Value("${app.keycloak.url}")
    public void setUrl(String url) {
        KEYCLOAK_URL = url;
    }

    @Value("${app.keycloak.url-admin}")
    public void setAdminUrl(String url) {
        KEYCLOAK_ADMIN_URL = url;
    }

}
