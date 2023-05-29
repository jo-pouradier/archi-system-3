package com.sp.model;

import com.sp.tools.Callback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
public class CardTemplate {

    @Id
    @GeneratedValue
    private UUID uuid;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    @Setter
    private String imageUrl;
    @Getter
    @Setter
    private String family;
    @Getter
    @Setter
    private String affinity;
    @Getter
    @Setter
    private Callback<Integer> hp;
    @Getter
    @Setter
    private Callback<Integer> attack;
    @Getter
    @Setter
    private Callback<Integer> defense;
    @Getter
    @Setter
    private Callback<Integer> energy;
    @Getter
    @Setter
    private Callback<Integer> price;

    public CardTemplate() {

    }

    public CardTemplate(String name, String description, String imageUrl, String family, String affinity, Callback<Integer> hp, Callback<Integer> attack, Callback<Integer> defense, Callback<Integer> energy, Callback<Integer> price) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.family = family;
        this.affinity = affinity;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.energy = energy;
        this.price = price;
    }

    public Card getRandomCard() {
        return Card.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .family(family)
                .affinity(affinity)
                .hp(hp.run())
                .attack(attack.run())
                .defense(defense.run())
                .energy(energy.run())
                .price(price.run())
                .build();
    }
}
