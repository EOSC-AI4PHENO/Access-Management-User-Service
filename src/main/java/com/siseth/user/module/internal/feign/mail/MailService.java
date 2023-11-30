package com.siseth.user.module.internal.feign.mail;


import com.siseth.user.module.internal.feign.api.request.CreateEmailsFeignReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "mail-service")
public interface MailService {

    @PostMapping("/api/internal/correspondence/mail/createEmail")
    String send(@RequestBody Map<String, Object> map);

    @PostMapping("/api/internal/correspondence/mail/createEmails")
    String sendEmails(@RequestBody CreateEmailsFeignReqDTO map);



}
