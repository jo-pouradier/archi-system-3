package com.sp.repository;

import com.sp.model.Card;
import com.sp.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<Card, UUID> {

    default List<Card> findByOwnerUUID(UUID uuid){
        List<Card> cards = new ArrayList<Card>();
        this.findAll().iterator().forEachRemaining(card -> {
            if(card.getOwnerUUID().equals(uuid)){
                cards.add(card);
            }
        });
        return cards;
    }

    default Card update(Card card) {
        return this.save(card);
    }
}
