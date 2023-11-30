package com.siseth.user.module.internal.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordReqDTO {

    private String email;

    private String password;

    private String access_token;
}
