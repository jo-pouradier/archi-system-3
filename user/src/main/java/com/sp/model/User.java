package com.sp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true, setterPrefix = "with")
@AllArgsConstructor
@Entity
public class User implements OwnerUUID{

    public static final User NULL_USER = User
            .builder()
            .withUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .withName("-1")
            .withEmail("-1")
            .withPassword("-1")
            .build();

    @Id
    @GeneratedValue
    private UUID uuid;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private float balance = 0;

    public User(){
    }

    public User(String name, String password, String email){
        this.password = password;
        this.name = name;
        this.email = email;
    }
    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

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
