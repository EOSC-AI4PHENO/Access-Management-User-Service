package com.siseth.user.module.keycloak.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupsDTO {

    private String id;
    private String name;
    private String path;
    private List<UserGroupsDTO> subGroups;
}
