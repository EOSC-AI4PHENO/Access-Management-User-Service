package com.siseth.user.component;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class UserModelClass {

    public enum UserRoles {

        RESTRICTED_USER ("Restricted Users"),
        REGULAR_USER ("Regular Users"),
        POWER_USER ("Power Users"),

        BLACKLISTED_USER ("Blacklisted Users");


        @Getter
        private String desc;

        UserRoles(String desc) {
            this.desc = desc;
        }

        public static Stream<UserRoles> stream() {
            return Stream.of(UserRoles.values());
        }




    }

}