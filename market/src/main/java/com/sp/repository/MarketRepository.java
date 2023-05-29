package com.sp.repository;

import com.sp.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarketRepository extends CrudRepository<Transaction, UUID> {

    default List<Transaction> findByOwnerUUID(UUID uuid){
        List<Transaction> transactions = new ArrayList<Transaction>();
        this.findAll().iterator().forEachRemaining(card -> {
            if(card.getFromUserUUID().equals(uuid)){
                transactions.add(card);
            }
        });
        return transactions;
    }


    default Transaction findByFromUserUUIDAndCardUUID(UUID from, UUID card, String status){
        for (Transaction transaction : this.findByOwnerUUID(from)) {
            if(transaction.getCardUUID().equals(card) && transaction.getStatus().equals(status)){
                return transaction;
            }
        }
        return null;
    }
}
