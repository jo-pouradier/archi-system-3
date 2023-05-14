package com.sp.model;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class CardDTO{

    private UUID uuid;
    private UUID ownerUUID;
    private String name;
    private String description;
    private String imageUrl;
    private String family;
    private String affinity;
    private int hp;
    private int attack;
    private int defense;
    private int energy;

    public CardDTO(){

    }

    // FIXME j'ai l'impression que ce constructeur est inutile
    public CardDTO(String name, String description, String imageUrl, String family, String affinity, int hp, int attack, int defense, int energy) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.family = family;
        this.affinity = affinity;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "uuid=" + uuid + '\'' +
                ", ownerUUID=" + ownerUUID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", family='" + family + '\'' +
                ", affinity='" + affinity + '\'' +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", energy=" + energy +
                '}';
    }
}
