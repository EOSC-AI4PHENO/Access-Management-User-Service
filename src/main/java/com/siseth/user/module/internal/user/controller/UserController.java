package com.siseth.user.module.internal.user.controller;

import com.siseth.user.component.UserModelClass;
import com.siseth.user.module.internal.feign.api.UserFormResDTO;
import com.siseth.user.module.internal.feign.api.request.CreateEmailsFeignReqDTO;
import com.siseth.user.module.internal.user.api.request.*;
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
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access/users")
@Tag(name = "Users Controller", description = "Endpoints to manage user account")
public class UserController {

    private final UserService service;


    @GetMapping("/roles")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(UserModelClass.UserRoles.stream());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/updateUserAccount")
    @Operation(summary = "Update user account", description = "Endpoint for updating user account")
    public ResponseEntity<String> updateUser(@Parameter(hidden = true) @RequestHeader(required = false) String token,
                                             @RequestBody UserWithIdUpdateReqDTO userWithIdUpdateReqDTO,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.updateUser(userWithIdUpdateReqDTO, token, realm));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/enableUserAccount")
    @Operation(summary = "Enable user account", description = "Endpoint for enabling user by admins")
    public ResponseEntity<String> enableUserAccount(@RequestParam String id,
                                                    @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                                    @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.enableUser(id, token, realm));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/disableUserAccount")
    @Operation(summary = "disable user account", description = "Endpoint for disabling user by admins")
    public ResponseEntity<String> disableUserAccount(@RequestParam String id,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.disableUser(id, token, realm));
    }

    @PostMapping("/createAccount")
    @Operation(summary = "Create account", description = "Endpoint for creating an account with an email")
    public ResponseEntity<String> createAccount(@RequestBody UserEmailReqDTO userEmailReqDTO,
                                                @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.createAccount(userEmailReqDTO, realm));
    }

    @PostMapping("/sendEmailToUsers")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create account", description = "Endpoint for creating an account with an email")
    public ResponseEntity<String> sendEmailToUsers(@RequestBody CreateEmailsReqDTO dto,
                                                   @Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                                   @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.sendEmailToUsers(dto, realm, token));
    }

    @PutMapping("/acceptUser")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Accept account", description = "Endpoint for accepting an account")
    public ResponseEntity<String> acceptUser(@RequestBody UserIdReqDTO userIdReqDTO,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.acceptUser(token, userIdReqDTO.getId(), realm));
    }

    @PutMapping("/rejectUser")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Reject account", description = "Endpoint for rejecting an account")
    public ResponseEntity<String> rejectUser(@RequestBody UserIdReqDTO userIdReqDTO,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.rejectUser(token, userIdReqDTO.getId(), realm));
    }

    @PutMapping("/setRole")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Set user role", description = "Endpoint for setting user role")
    public ResponseEntity<String> setRole(@RequestBody UserIdReqDTO userIdReqDTO,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                             @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.setRole(token, userIdReqDTO, realm));
    }

    @PostMapping("/sendActivationLinkAgain")
    @Operation(summary = "Send Activation link", description = "Send activation link for the given email")
    public ResponseEntity<String> activateAccount(@RequestBody UserEmailReqDTO userEmailReqDTO,
                                                  @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.sendActivationLinkAgain(userEmailReqDTO, realm));
    }

    @PostMapping("/resetUserAccountPassword")
    @Operation(summary = "Reset password", description = "Endpoint that reset password of the user account")
    public ResponseEntity<String> resetPassword(@RequestBody UserEmailReqDTO userEmailReqDTO,
                                                @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.resetPasswordForCreatedUser(userEmailReqDTO, realm, "RESET_PASSWORD"));
    }

    @PutMapping("/changeUserAccountPasswordWhileNotLogged")
    @Operation(summary = "Change user password", description = "Endpoint for changing password for the user")
    public ResponseEntity<KeyCloakSignInDTO> changePassword(@RequestBody ChangePasswordReqDTO changePasswordReqDTO,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.changePassword(changePasswordReqDTO, changePasswordReqDTO.getAccess_token(), realm));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/changeUserAccountPassword")
    @Operation(summary = "Change user password", description = "Endpoint for changing password for the logged user")
    public ResponseEntity<String> changePasswordWhileLogged(@RequestBody ChangePasswordWithIdReqDTO changePasswordWithIdReqDTO,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.changePassword(changePasswordWithIdReqDTO, realm));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getUserInfoById")
    @Operation(summary = "Get user information", description = "Get user information by id")
    public ResponseEntity<KeyCloakUserInfoResDTO> getUserInfoById(@RequestParam String id,
                                                                  @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.getUserInfoById(id, realm));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getAllUsers")
    @Operation(summary = "Get all users", description = "Get all user information")
    public ResponseEntity<List<UserFormResDTO>> getAllUsers(@RequestParam(required = false) String role,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                                            @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.getAllUsers(role, realm, token));
    }



    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getUserInfoByToken")
    @Operation(summary = "Get user information", description = "Get user information by id")
    public ResponseEntity<UserInfoWithRolesResDTO> getUserInfoByToken(@Parameter(hidden = true) @RequestHeader(required = false) String realm,
                                                                      @Parameter(hidden = true) @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(service.getUserInfoByToken(token, realm));
    }

  }