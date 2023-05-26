package fr.dtos.common.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginFormDTO {

    private String email;
    private String password;

    public LoginFormDTO() {
    }

    public LoginFormDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
