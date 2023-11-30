package com.siseth.user.module.internal.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordWithIdReqDTO {

    private String email;

    private String oldPassword;

    private String newPassword;

    public Boolean samePassword() {
        return oldPassword.equals(newPassword);
    }
}
