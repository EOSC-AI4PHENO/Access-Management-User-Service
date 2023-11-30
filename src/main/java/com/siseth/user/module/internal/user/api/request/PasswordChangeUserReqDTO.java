package com.siseth.user.module.internal.user.api.request;

import com.siseth.user.module.internal.user.api.UserCredentialsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeUserReqDTO {
    List<UserCredentialsDTO> credentials;

    public PasswordChangeUserReqDTO(UserCredentialsDTO credentials) {
        this.credentials = new ArrayList<>();
        this.credentials.add(credentials);
    }
}
