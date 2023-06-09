package fr.dtos.common.fight;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class FightDTO {

    @Getter
    @Setter
    private UUID fightUUID = null;
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
}
