package com.sp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public
class UserRegisterDTO {
    private String username;
    private String password;
    private String email;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}