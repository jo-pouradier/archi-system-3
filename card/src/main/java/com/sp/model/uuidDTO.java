package com.sp.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class uuidDTO {

    private UUID uuid;

    public uuidDTO(){
    }
    public uuidDTO(UUID uuid){
        this.uuid = uuid;
    }
}
