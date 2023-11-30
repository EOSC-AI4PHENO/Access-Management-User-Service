package com.siseth.user.module.internal.feign.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFormResDTO {

    private String userId;
    private String firstName;
    private String lastName;
    private String organisation;
    private String country;
    private String position;
    private String scope;
    private List<String> purpose;
    private String project;
    private OffsetDateTime submissionDate;
    private String email;
    private String role;
    private Boolean enabled;
}
