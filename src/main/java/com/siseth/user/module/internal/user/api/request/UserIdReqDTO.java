package com.siseth.user.module.internal.user.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siseth.user.component.UserModelClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserIdReqDTO {

    private String id;

    private UserModelClass.UserRoles role;

}
