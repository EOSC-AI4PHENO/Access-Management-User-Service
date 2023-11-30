package com.siseth.user.module.internal.feign.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInReqDTO {

    private String username;

    private String password;
}
