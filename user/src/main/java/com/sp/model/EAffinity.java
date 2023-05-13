package com.sp.model;

import lombok.Getter;

public enum EAffinity {

    FIRE("Fire"),
    WATER("Water"),
    EARTH("Earth"),
    AIR("Air"),
    SPACE("Space"),
    TIME("Time"),
    TAMER("Tamer"),
    ;
    @Getter
    private String name;
    EAffinity(String name) {
        this.name = name;
    }
}
