package com.sp.model;

import lombok.Getter;

public enum EFamily {

    SUPER_HEROES("Super Heroes"),
    SUPER_VILLAINS("Super Villains"),
    ;
    @Getter
    private String name;
    EFamily(String name) {
        this.name = name;
    }
}
