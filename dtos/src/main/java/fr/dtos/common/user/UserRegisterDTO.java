package fr.dtos.common.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Builder
public
class UserRegisterDTO {
    @Getter
    private UUID uuid;
    @Getter
    private String name;
    @Getter
    private String password;
    @Getter
    private String email;

    public UserRegisterDTO() {
    }
    public UserRegisterDTO(UUID uuid, String name, String password, String email) {
        this.uuid = uuid;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserRegisterDTO(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}