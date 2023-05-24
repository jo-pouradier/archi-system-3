package fr.dtos.common.user;

import fr.dtos.common.utils.PairDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class UserAndDataDTO<T> {
    @Getter
    @Setter
    UUID user;

    @Getter
    @Setter
    T data;

    @Override
    public String toString() {
        return "UserAndDataDTO{" +
                "user=" + user +
                ", data=" + data +
                '}';
    }
}
