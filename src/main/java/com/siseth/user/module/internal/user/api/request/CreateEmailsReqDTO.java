package com.siseth.user.module.internal.user.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateEmailsReqDTO {
    private String subject;
    private String message;
    private List<String> emails;
}
