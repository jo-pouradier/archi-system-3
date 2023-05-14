package com.sp.model;

import lombok.*;

import java.util.UUID;

@Builder(toBuilder = true, setterPrefix = "with")
@AllArgsConstructor
@Data
public class UserDTO { //implements OwnerUUID

    public static final UserDTO NULL_USER = UserDTO
            .builder()
            .withUuid("00000000-0000-0000-0000-000000000000")
            .withName("-1")
            .withEmail("-1")
            .withPassword("-1")
            .build();

    private String uuid;
    private String name;
    private String email;
    private String password;
    private float balance = 0.0F;

    public UserDTO(){
    }

    public UserDTO(String name, String password, String email){
        this.password = password;
        this.name = name;
        this.email = email;
    }
//    @Override
//    public UUID getUUID() {
//        return this.uuid;
//    }
//
//    @Override
//    public void setUUID(UUID uuid) {
//        this.uuid = uuid;
//    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
