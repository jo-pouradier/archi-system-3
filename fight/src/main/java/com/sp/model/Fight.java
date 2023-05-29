package com.sp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Fight {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private UUID transcationUUID = null;
    @Getter
    @Setter
    private UUID fromCardUUID = null;
    @Getter
    @Setter
    private UUID fromUserUUID = null;
    @Getter
    @Setter
    private UUID toCardUUID = null;
    @Getter
    @Setter
    private UUID toUserUUID = null;
    @Getter
    @Setter
    private float price = 0;
    @Getter
    @Setter
    private String status = "uncomplete";


    public boolean isAccepted() {
        return status.equals("accepted");
    }

    public boolean isPending() {
        return status.equals("pending");
    }

    public boolean isUncomplete() {
        return status.equals("uncomplete");
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transcationUUID=" + transcationUUID +
                ", fromCardUUID=" + fromCardUUID +
                ", fromUserUUID=" + fromUserUUID +
                ", toCardUUID=" + toCardUUID +
                ", toUserUUID=" + toUserUUID +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
