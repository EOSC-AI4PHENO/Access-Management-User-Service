package com.siseth.user.module.internal.user.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEmailWithTokenReqDTO extends UserEmailReqDTO{

    private AttributesToken attributes;



    public UserEmailWithTokenReqDTO(UserEmailReqDTO dto, String newUserToken) {
        super(dto.getEmail());
        this.attributes = new AttributesToken(newUserToken, String.valueOf(OffsetDateTime.now()));
    }

     public String getNewUserToken() {
        return attributes.getNewUserToken();
    }

    public String gettokenExpireTime() {
        return attributes.getTokenExpireTime();
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class AttributesToken {
    private String newUserToken;
    private String tokenExpireTime;
}



