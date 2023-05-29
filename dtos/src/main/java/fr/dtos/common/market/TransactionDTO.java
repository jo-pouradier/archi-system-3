package fr.dtos.common.market;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class TransactionDTO {

    @Getter
    @Setter
    private UUID transcationUUID = null;
    @Getter
    @Setter
    private UUID cardUUID = null;
    @Getter
    @Setter
    private UUID fromUserUUID = null;
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
}
