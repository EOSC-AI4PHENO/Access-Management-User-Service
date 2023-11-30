package com.siseth.user.module.internal.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.keycloak")
public class ApplicationProps {

    private List<Realm> realms;

    @Getter
    @Setter
    public static class Realm {
        private String name;
        private String client_id;
        private String client_secret;
    }

    public Realm getRealm(String name) {
        return this.realms.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Realm not found!"));
    }

}
