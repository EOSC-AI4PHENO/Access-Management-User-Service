package com.siseth.user.module.internal.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendTokenDTO {

    private String email;

    private String token;
}
