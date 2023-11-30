package com.siseth.user.module.internal.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.user.component.UserModelClass;
import com.siseth.user.module.internal.feign.api.UserFormResDTO;
import com.siseth.user.module.internal.feign.api.request.CreateEmailsFeignReqDTO;
import com.siseth.user.module.internal.feign.api.request.UserSignInReqDTO;
import com.siseth.user.module.internal.feign.authentication.AuthenticationFeign;
import com.siseth.user.module.internal.feign.forms.FormsFeign;
import com.siseth.user.module.internal.feign.mail.MailService;
import com.siseth.user.module.internal.property.ApplicationProps;
import com.siseth.user.module.internal.user.api.TokenDTO;
import com.siseth.user.module.internal.user.api.UserRolesDTO;
import com.siseth.user.module.internal.user.api.request.*;
import com.siseth.user.module.internal.user.api.response.UserInfoWithRolesResDTO;
import com.siseth.user.module.internal.user.components.UserEmailUtils;
import com.siseth.user.module.internal.user.exception.NotAcceptableException;
import com.siseth.user.module.internal.user.exception.NotFoundException;
import com.siseth.user.module.internal.user.exception.ServerException;
import com.siseth.user.module.keycloak.api.*;
import com.siseth.user.module.keycloak.model.*;
import com.siseth.user.module.service.EurekaService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EurekaService eurekaService;

    private final AuthenticationFeign authenticationFeign;

    private final FormsFeign formsFeign;

    private final MailService mailService;

    private final ApplicationProps props;

    private List<UserFormResDTO> list;
    private final String ACTIVATE_ACCOUNT_MAIL = "ACTIVATE_ACCOUNT";

    public String updateUser(UserWithIdUpdateReqDTO userWithIdUpdateReqDTO, String access_token, String realm) {
        return new UpdateUser(userWithIdUpdateReqDTO, access_token, realm).getResponse();
    }

    public String enableUser(String id, String access_token, String realm) {
        return new EnableUser(id, access_token, true, realm).getResponse();
    }

    public String disableUser(String id, String access_token, String realm) {
        return new EnableUser(id, access_token, false, realm).getResponse();
    }

    public KeyCloakSignInDTO changePassword(ChangePasswordReqDTO dto, String newUserToken, String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();
        String userId = new ChangePassword(dto, token.getAccess_token(), newUserToken, realm).getResponse();

        String userGroup = new GetUserGroup(token.getAccess_token(), realm, userId).getResponse();

        if (userGroup.equals("Pre-Registered Users"))
            new ChangeUserGroup(token.getAccess_token(), realm, userId, "Pre-Registered Users", "Restricted Users").getResponse();

        return authenticationFeign.issueSessionToken(new UserSignInReqDTO(dto.getEmail(), dto.getPassword()), realm);
    }

    public String changePassword(ChangePasswordWithIdReqDTO dto, String realm) {
        if(dto.samePassword())
            throw new ServerException("New password is the same as old password");

        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        return new ChangePassword(dto, token.getAccess_token(), realm).getResponse();
    }

    public String createAccount(UserEmailReqDTO dto, String realm) {
        if (!UserEmailUtils.valid(dto.getEmail()))
            throw new NotAcceptableException("Email is not correct");

        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();
        String response = new CreateUser(dto, token.getAccess_token(), realm).getResponse();
        resetPassword(dto, realm, ACTIVATE_ACCOUNT_MAIL);
        return response;
    }

    public String sendEmailToUsers(CreateEmailsReqDTO dto, String realm, String token) {
        List<PreRegisterUsersDTO> combinedUsers = new ArrayList<>();
        combinedUsers.addAll(new GetUsersFromGroup(token, realm, "Power Users").getResponse());
        combinedUsers.addAll(new GetUsersFromGroup(token, realm, "Regular Users").getResponse());
        combinedUsers.addAll(new GetUsersFromGroup(token, realm, "Admin Users").getResponse());


        List<String> emails = !dto.getEmails().isEmpty() ?
                dto.getEmails() : combinedUsers.stream().map(PreRegisterUsersDTO::getEmail).collect(Collectors.toList());

        Map<String, String> map = Map.of("realm", realm,
                                         "subject", dto.getSubject(),
                                         "message", dto.getMessage(),
                                         "type", "DEFAULT");

        return mailService.sendEmails(new CreateEmailsFeignReqDTO(map, emails));
    }

    public String acceptUser(String token, String id, String realm) {
        new ChangeUserGroup(token, realm, id, "Restricted Users", "Power Users").getResponse();

        formsFeign.acceptForm(realm, id);

        TokenDTO tokenAdmin = new TokenAuth(props.getRealm(realm)).getResponse();
        KeyCloakUserInfoResDTO user = new UserInfo(id, tokenAdmin.getAccess_token(), realm).getResponse();

        sendEmailToUserId(id, realm,
                "Congratulations! We are thrilled to inform you that your registration has been approved by our administration team. Welcome to the AI4PHENO community!",
                "Welcome Aboard – Your Registration has been Approved!");

        return "Successfully accepted user of id: " + id;
    }

    public void sendEmailToUserId(String id, String realm, String message, String subject) {
        TokenDTO tokenAdmin = new TokenAuth(props.getRealm(realm)).getResponse();
        KeyCloakUserInfoResDTO user = new UserInfo(id, tokenAdmin.getAccess_token(), realm).getResponse();

        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("realm", realm);
        map.put("type", "MISSING_FILE");
        map.put("subject", subject);
        map.put("desc", message);

        mailService.send(map);
    }

    public String rejectUser(String token, String id, String realm) {
//        new ChangeUserGroup(token, realm, id, "Restricted Users", "Blacklisted Users").getResponse();

        formsFeign.rejectForm(realm, id);

        sendEmailToUserId(id, realm,
                "Thank you for your interest in the AI4PHENO community. We regret to inform you that after careful consideration, your registration has not been approved by our administration team at this time. We encourage you to review our community guidelines and eligibility criteria, and consider reapplying in the future. We appreciate your understanding.",
                "Registration Update – Your Application Status");

        return "Successfully rejected user of id: " + id;
    }

    public String setRole(String token, UserIdReqDTO dto, String realm) {
        String group = new GetUserGroup(token, realm, dto.getId()).getResponse();


        new ChangeUserGroup(token, realm, dto.getId(), group, dto.getRole().getDesc()).getResponse();

        return "Successfully changed role for user of id: " + dto.getId();
    }


    public String resetPasswordForCreatedUser(UserEmailReqDTO dto, String realm, String type) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();
        String userId = String.valueOf(new GetUserId(token.getAccess_token(), dto.getEmail(), realm).getResponse());

        String userGroup = new GetUserGroup(token.getAccess_token(), realm, userId).getResponse();

        if (userGroup.equals("Pre-Registered Users"))
            throw new RuntimeException("User dont have password");

        return resetPassword(dto, realm, type);
    }

    @SneakyThrows
    public String resetPassword(UserEmailReqDTO dto, String realm, String type) {
        try{
            TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

            Map<String,Object> att = new GetAttributes(dto, token.getAccess_token(), realm).getResponse();

            String generatedToken = new ResetPassword(dto, token.getAccess_token(), att, realm).getResponse();

            sendEmail(dto.getEmail(), generatedToken, realm, type);

        } catch (Exception e) {
            e.getStackTrace();
            return e.getMessage();
        }

        return "OK";
    }

    @SneakyThrows
    public void sendEmail(String email, String generatedToken, String realm, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("realm", realm);
        map.put("type", type);
        map.put("token", generatedToken);
        map.put("subject", type.equals(ACTIVATE_ACCOUNT_MAIL) ?
                                                        "Activate Your Account!" :
                                                        "Reset password!");

        mailService.send(map);
    }

    public String sendActivationLinkAgain(UserEmailReqDTO dto, String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        List<PreRegisterUsersDTO> users = new GetUsersFromGroup(token.getAccess_token(), realm, "Pre-Registered Users").getResponse();
        users.forEach(user -> {
            if (user.getEmail().equals(dto.getEmail())) {
                sendEmail(dto.getEmail(), user.getToken(), realm, ACTIVATE_ACCOUNT_MAIL);
            }else {
                throw new NotFoundException("USER NOT FOUND");
            }
        });

        return "OK";
    }

    public KeyCloakUserInfoResDTO getUserInfoById(String id, String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        return new UserInfo(id, token.getAccess_token(), realm).getResponse();
    }

    public List<KeyCloakUserInfoResDTO> getUserInfoByIds(List<String> ids, String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        return ids.stream().map(id -> {
                    try {
                        return new UserInfo(id, token.getAccess_token(), realm).getResponse();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<UserFormResDTO> getAllUsers(String role, String realm, String token) {

        List<PreRegisterUsersDTO> powerUsers = new GetUsersFromGroup(token, realm, "Power Users").getResponse();
        List<PreRegisterUsersDTO> regularUsers = new GetUsersFromGroup(token, realm, "Regular Users").getResponse();
        List<PreRegisterUsersDTO> restrictedUsers = new GetUsersFromGroup(token, realm, "Restricted Users").getResponse();
        List<PreRegisterUsersDTO> blacklistedUsers = new GetUsersFromGroup(token, realm, "Blacklisted Users").getResponse();

        list = new ArrayList<>();
        return _addUsers("Blacklisted User", realm, blacklistedUsers,
                _addUsers("Restricted User", realm, restrictedUsers,
                _addUsers("Regular User", realm, regularUsers,
                _addUsers("Power User", realm, powerUsers, list)))).stream()
                .filter(x -> role == null || x.getRole().equals(role))
                .sorted(Comparator.comparing(UserFormResDTO::getSubmissionDate).reversed())
                .collect(Collectors.toList());
    }

    private List<UserFormResDTO> _addUsers(String role, String realm, List<PreRegisterUsersDTO> users, List<UserFormResDTO> ls) {
        for (PreRegisterUsersDTO user : users) {
            try {
                UserFormResDTO form = formsFeign.getForm(user.getId(), realm);
                form.setEmail(user.getEmail());
                form.setRole("Power User");
                form.setRole(role);
                form.setEnabled(user.getEnabled());
                ls.add(form);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return ls;
    }
    @SneakyThrows
    public UserInfoWithRolesResDTO getUserInfoByToken(String access_token, String realm) {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(access_token.split("\\.")[1]));

        UserRolesDTO roles =  new ObjectMapper().readValue(payload, UserRolesDTO.class);
        KeyCloakUserInfoResDTO keyCloakUserInfoResDTO = getUserInfoById(roles.getSub(), realm);
        UserInfoWithRolesResDTO res = new UserInfoWithRolesResDTO(keyCloakUserInfoResDTO, roles.getRealm_access());
        return res;
    }

    public List<PreRegisterUsersDTO> getPreRegisterUsers(String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        return new GetUsersFromGroup(token.getAccess_token(), realm, "Pre-Registered Users").getResponse();
    }

    public String deleteUser(String userId, String realm) {
        TokenDTO token = new TokenAuth(props.getRealm(realm)).getResponse();

        return new DeleteUser(token.getAccess_token(), userId, realm).getResponse();
    }
}
