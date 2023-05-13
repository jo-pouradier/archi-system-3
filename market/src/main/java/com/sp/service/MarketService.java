package com.sp.service;

import com.sp.model.Card;
import com.sp.model.Transaction;
import com.sp.model.User;
import com.sp.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MarketService {

    @Autowired
    private MarketRepository market;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketRepository marketRepository;

    public Transaction createTransaction(Transaction transaction) {
        User from = userService.getUser(transaction.getFromUserUUID());
        Card card = cardService.getCard(transaction.getCardUUID());
        if (from != null && card != null) {
            Transaction exist = getByFromAndCard(from.getUUID(), card.getUUID(), "pending");
            if (exist != null && exist.isPending())
                return null;
            if (cardService.getCardsByOwnerUUID(from.getUUID()).contains(card)) {
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
                valid.getCardUUID().equals(transaction.getCardUUID()) && valid.isPending() && valid.getToUserUUID().equals(transaction.getToUserUUID()))
            return true;
        return false;
    }

    public Transaction cancelTransaction(Transaction transaction) {
        if (!isValidCancelTransaction(transaction))
            return null;
        User from = userService.getUser(transaction.getFromUserUUID());
        Card card = cardService.getCard(transaction.getCardUUID());
        if (from != null && card != null) {
            if (cardService.getCardsByOwnerUUID(from.getUUID()).contains(card)) {
                transaction.setStatus("canceled");
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
        User from = userService.getUser(transaction.getFromUserUUID());
        User to = userService.getUser(transaction.getToUserUUID());
        Card card = cardService.getCard(transaction.getCardUUID());
        if (from != null && card != null && to != null) {
            Transaction valid =  getByFromAndCard(from.getUUID(), card.getUUID(), "pending");
            if (valid == null)
                return null;
            if (valid.getPrice() > to.getBalance())
                return null;
            if (cardService.getCardsByOwnerUUID(from.getUUID()).contains(card)) {
                transaction.setStatus("accepted");
                cardService.changeOwner(card, to);
                userService.debit(to.getUUID(), valid.getPrice());
                userService.depot(from.getUUID(), valid.getPrice());
                return transaction;
            }
        }
        return null;
    }
}
