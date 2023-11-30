package com.siseth.user.module.internal.feign.forms;


import com.siseth.user.module.internal.feign.api.UserFormResDTO;
import com.siseth.user.module.keycloak.api.KeyCloakSignInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "forms-service")
public interface FormsFeign {


    @PutMapping("/api/inquiry-facility/forms/acceptForm")
    String acceptForm(@RequestParam String realm, @RequestParam String userId);


    @PutMapping("/api/inquiry-facility/forms/rejectForm")
    String rejectForm(@RequestParam String realm, @RequestParam String userId);

    @GetMapping("/api/internal/forms/getUserInfo")
    UserFormResDTO getForm(@RequestParam String id, @RequestParam String realm);

}
