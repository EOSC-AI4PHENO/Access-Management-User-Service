package com.siseth.user.module.internal.user.schedule;

import com.siseth.user.module.internal.property.ApplicationProps;
import com.siseth.user.module.internal.user.constant.AppConstant;
import com.siseth.user.module.internal.user.service.UserService;
import com.siseth.user.module.keycloak.api.PreRegisterUsersDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class DeleteInactiveUsersSchedule {

    private final UserService service;


    private final ApplicationProps props;

    @Scheduled(initialDelay = 10000, fixedDelay = 6000000)
    public void getUsers() {
        log.debug("Start schedule");
        props.getRealms().forEach(x -> _getUsers(x.getName()));
    }

    private void _getUsers(String realm) {
        List<PreRegisterUsersDTO> users = service.getPreRegisterUsers(realm);
        log.info("Users Schedule");
        if (users.size() > 0) {
            users.forEach(user -> {
                if (user.tokenExpired()) {
                    this.deleteUserAccount(user, realm);
                    log.info("realm: "+ realm +" token for user: " + user.getId() + " is expired");
                }else {
                    log.info("realm: "+ realm +" token for user: " + user.getId() + " is valid");
                }
            });
        }
    }

    private void deleteUserAccount(PreRegisterUsersDTO user, String realm) {
        String response = service.deleteUser(user.getId(), realm);
    }

}
