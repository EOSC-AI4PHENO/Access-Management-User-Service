package com.siseth.user.module.internal.user.api.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siseth.user.module.internal.user.api.RolesReqDTO;
import com.siseth.user.module.keycloak.api.KeyCloakUserInfoResDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoWithRolesResDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
//    private List<String> roles;
    private String role;


    public UserInfoWithRolesResDTO(KeyCloakUserInfoResDTO dto, RolesReqDTO roleDto) {
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.role = filterRoles(roleDto.getRoles());
    }

    private String filterRoles(List<String> roles) {
        if (roles.contains("Admin"))
            return "Admin";
        if (roles.contains("Power User"))
            return "Power User";
        if (roles.contains("Regular User"))
            return "Regular User";
        if (roles.contains("Restricted User"))
            return "Restricted User";
        return "";
    }
}

