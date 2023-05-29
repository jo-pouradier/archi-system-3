package com.sp.service;

import com.sp.model.Transaction;
import com.sp.repository.MarketRepository;
import fr.dtos.common.card.CardDTO;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MarketService {

    @Autowired
    private MarketRepository market;

    @Autowired
    private MarketRepository marketRepository;

    public Transaction createTransaction(Transaction transaction) {
        UserDTO from = Utils.getUser(transaction.getFromUserUUID());
        CardDTO card = Utils.getCard(transaction.getCardUUID());
        if (from != null && card != null) {
            Transaction exist = getByFromAndCard(from.getUUID(), card.getUuid(), "pending");
            if (exist != null && exist.isPending())
                return null;
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(card)) {
                transaction.setStatus("pending");
                transaction = marketRepository.save(transaction);
                return transaction;
            }
        }
        return null;
    }

    public Transaction getByFromAndCard(UUID from, UUID card, String status) {
        return marketRepository.findByFromUserUUIDAndCardUUID(from, card, status);
    }

    public boolean existTransaction(UUID from, UUID card) {
        return getByFromAndCard(from, card, "pending") != null;
    }

    public boolean isValidCancelTransaction(Transaction transaction) {
        if (transaction.getTranscationUUID() == null)
            return false;
        Transaction valid = marketRepository.findById(transaction.getTranscationUUID()).orElse(null);
        if (valid == null)
            return false;
        if (valid.getFromUserUUID().equals(transaction.getFromUserUUID()) &&
                valid.getCardUUID().equals(transaction.getCardUUID()))
            return true;
        return false;
    }

    public boolean isValidAcceptTransaction(Transaction transaction) {
        Transaction valid = marketRepository.findById(transaction.getTranscationUUID()).orElse(null);
        if (valid == null)
            return false;
        if (valid.getFromUserUUID().equals(transaction.getFromUserUUID()) &&
                valid.getCardUUID().equals(transaction.getCardUUID()) && valid.isPending())
            return true;
        return false;
    }

    public Transaction cancelTransaction(Transaction transaction) {
        if (!isValidCancelTransaction(transaction))
            return null;
        UserDTO from = Utils.getUser(transaction.getFromUserUUID());
        CardDTO card = Utils.getCard(transaction.getCardUUID());
        if (from != null && card != null) {
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(card)) {
                transaction.setStatus("canceled");
                marketRepository.save(transaction);
                marketRepository.delete(transaction);
                return transaction;
            }
        }
        return null;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        market.findAll().iterator().forEachRemaining(transactions::add);
        return transactions;
    }

    public Transaction acceptTransaction(Transaction transaction) {
        if (!isValidAcceptTransaction(transaction))
            return null;
        UserDTO from = Utils.getUser(transaction.getFromUserUUID());
        UserDTO to = Utils.getUser(transaction.getToUserUUID());
        CardDTO card = Utils.getCard(transaction.getCardUUID());
        if (from != null && card != null && to != null) {
            Transaction valid =  getByFromAndCard(from.getUUID(), card.getUuid(), "pending");
            if (valid == null)
                return null;
            if (valid.getPrice() > to.getBalance())
                return null;
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(card)) {
                transaction.setStatus("accepted");
                Utils.changeOwner(card, to);
                Utils.debit(to.getUUID(), valid.getPrice());
                Utils.depot(from.getUUID(), valid.getPrice());
                marketRepository.save(valid);
                marketRepository.delete(valid);
                System.out.println("Transaction accepted");
                return transaction;
            }
        }
        return null;
    }
}
