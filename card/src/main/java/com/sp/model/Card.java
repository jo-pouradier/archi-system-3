package com.sp.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;
@Builder(toBuilder = true)
@AllArgsConstructor
@Entity
@Data
public class Card { //implements OwnerUUID<UUID> {

    public static final Card NULL_CARD = Card
            .builder()
            .uuid(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .name("404")
            .description("404")
            .imageUrl("https://ih1.redbubble.net/image.1198305180.8026/papergc,500x,w,f8f8f8-pad,750x1000,f8f8f8.jpg")
            .family("404")
            .affinity("404")
            .hp(0)
            .attack(0)
            .defense(0)
            .energy(0)
            .build();

    @Id
    @GeneratedValue
    private UUID uuid;
    private UUID ownerUUID;

    // SET as VARCHAR 1024
    @Column(length = 1024)
    private String name;
    private String description;
    private String imageUrl;
    private String family;
    private String affinity;
    private int hp;
    private int attack;
    private int defense;
    private int energy;

    public Card(){
    }

    public Card(String name, String description, String imageUrl, String family, String affinity, int hp, int attack, int defense, int energy) {
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
        return "Card{" +
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
