package com.siseth.user.module.internal.feign.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CreateEmailsFeignReqDTO {
    private Map<String, String> map;
    private List<String> emails;


}
