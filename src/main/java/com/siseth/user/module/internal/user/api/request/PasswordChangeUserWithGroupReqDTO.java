package com.siseth.user.module.internal.user.api.request;

import com.siseth.user.module.internal.user.api.UserCredentialsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PasswordChangeUserWithGroupReqDTO extends PasswordChangeUserReqDTO{
    List<String> groups;

    public PasswordChangeUserWithGroupReqDTO(UserCredentialsDTO credentials) {
        super(credentials);
        this.groups = new ArrayList<>(List.of("/Users/Regular Users"));
    }
}
