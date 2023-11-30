package com.siseth.user.module.internal.user.controller;

import com.siseth.user.module.internal.user.api.request.ChangePasswordReqDTO;
import com.siseth.user.module.internal.user.api.request.ChangePasswordWithIdReqDTO;
import com.siseth.user.module.internal.user.api.request.UserEmailReqDTO;
import com.siseth.user.module.internal.user.api.request.UserWithIdUpdateReqDTO;
import com.siseth.user.module.internal.user.api.response.UserInfoWithRolesResDTO;
import com.siseth.user.module.internal.user.service.UserService;
import com.siseth.user.module.keycloak.api.KeyCloakSignInDTO;
import com.siseth.user.module.keycloak.api.KeyCloakUserInfoResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/access/users")
@Tag(name = "Users Controller", description = "Endpoints to manage user account")
public class UserInternalController {

    private final UserService service;

    @GetMapping("/getUserInfo")
    @Operation(summary = "Get user information", description = "Get user information by id")
    public ResponseEntity<KeyCloakUserInfoResDTO> getUserInfoById(@RequestParam String id,
                                                                  @RequestParam String realm) {
        return ResponseEntity.ok(service.getUserInfoById(id, realm));
    }

    @PostMapping("/getUsersInfo")
    @Operation(summary = "Get user information", description = "Get user information by id")
    public ResponseEntity<List<KeyCloakUserInfoResDTO>> getUserInfoById(@RequestBody List<String> id,
                                                                  @RequestParam String realm) {
        return ResponseEntity.ok(service.getUserInfoByIds(id, realm));
    }

  }