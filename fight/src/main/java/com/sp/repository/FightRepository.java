package com.sp.repository;

import com.sp.model.Fight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface FightRepository extends CrudRepository<Fight, UUID> {

    default List<Fight> findByOwnerUUID(UUID uuid){
        List<Fight> transactions = new ArrayList<Fight>();
        this.findAll().iterator().forEachRemaining(card -> {
            if(card.getFromUserUUID().equals(uuid)){
                transactions.add(card);
            }
        });
        return transactions;
    }


    default Fight findByFromUserUUIDAndCardUUID(UUID from, UUID card, String status){
        for (Fight fight : this.findByOwnerUUID(from)) {
            if(fight.getToCardUUID().equals(card) && fight.getStatus().equals(status)){
                return fight;
            }
        }
        return null;
    }
}
