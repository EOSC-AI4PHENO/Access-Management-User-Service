package com.siseth.user.module.internal.user.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppConstant {

    public static String KEYCLOAK_URL;

    @Value("${app.keycloak.url}")
    public void setUrl(String url) {
        KEYCLOAK_URL = url;
    }

}
