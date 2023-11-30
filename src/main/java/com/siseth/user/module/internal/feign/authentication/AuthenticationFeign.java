package com.siseth.user.module.internal.feign.authentication;

import com.siseth.user.module.internal.feign.api.request.UserSignInReqDTO;
import com.siseth.user.module.keycloak.api.KeyCloakSignInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authentication-service")
    public interface AuthenticationFeign {

        @PostMapping("/api/access/authentication/issueSessionToken")
        KeyCloakSignInDTO issueSessionToken(@RequestBody UserSignInReqDTO userSignInReqDTO, @RequestHeader String realm);

    }
