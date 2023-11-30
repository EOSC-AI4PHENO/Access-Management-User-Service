package com.siseth.user.module.internal.user.api.request;

import com.siseth.user.module.internal.user.api.AttributesDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithIdUpdateReqDTO extends UserUpdateReqDTO {

    private String id;
}
