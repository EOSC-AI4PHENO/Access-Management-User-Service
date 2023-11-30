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
public class CreateUserReqDTO extends UserEmailReqDTO{

    private AttTermsOfUse attributes;
    private List<String> groups;
    private Boolean enabled;
    private Boolean emailVerified;


    public CreateUserReqDTO(UserEmailReqDTO dto) {
        super(dto.getEmail());
        this.enabled = true;
        this.emailVerified = true;
        this.attributes = new AttTermsOfUse(true, true);
        this.groups = new ArrayList<>(List.of("/Users/Pre-Registered Users"));
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class AttTermsOfUse {
    private Boolean termsOfUseAccepted;
    private Boolean privacyPolicyAccepted;
}


