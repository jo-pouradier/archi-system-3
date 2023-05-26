package fr.dtos.common.auth;

import lombok.Getter;

public enum AuthType {

    NO_AUTH(false),
    USER(false),
    SERVICE(true);

    @Getter
    boolean service;

    AuthType (boolean isService) {
        this.service = isService;
    }
}
