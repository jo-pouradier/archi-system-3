package com.sp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class loginFormDTO {

    private String email;
    private String password;

    public loginFormDTO() {
    }

    public loginFormDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
