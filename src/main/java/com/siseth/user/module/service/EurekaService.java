package com.siseth.user.module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EurekaService {

    private final DiscoveryClient discoveryClient;

    private final String USERS_SERVICE = "USERS-SERVICE";

    private final String CORRESPONDENCE_GATEWAY_SERVICE = "CORRESPONDENCE-GATEWAY-SERVICE";

    public Optional<ServiceInstance> getInstance(String instanceName) {
        return discoveryClient
                .getInstances(instanceName)
                .stream()
                .findAny();
    }

    public List<String> getInstances() {
        return discoveryClient.getServices();
    }

    public ServiceInstance getCorrespondenceGatewayService() {
        return getInstance(CORRESPONDENCE_GATEWAY_SERVICE).orElseThrow(() -> new RuntimeException("User Service not found!"));
    }

}
