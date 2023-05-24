package fr.dtos.common.utils;

import lombok.Getter;
import lombok.Setter;

public class AdminKeyDataDTO<T> {

    @Getter
    @Setter
    private String admin_key;
    @Getter
    @Setter
    private T data;

    public boolean isAdminKeyValid() {
        if (!Utils.isSuperAdminKey(this.getAdmin_key())) {
            throw new ForbiddenException("Admin key is not valid");
        }
        return true;
    }

    @Override
    public String toString() {
        return "AdminKeyDataDTO{" +
                "admin_key='" + admin_key + '\'' +
                ", data=" + data +
                '}';
    }
}
