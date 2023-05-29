package com.sp.service;

import com.sp.model.Fight;
import com.sp.repository.FightRepository;
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
public class FightService {

    @Autowired
    private FightRepository fightRepository;


    public Fight createFight(Fight fight) {
        UserDTO from = Utils.getUser(fight.getFromUserUUID());
        CardDTO card = Utils.getCard(fight.getFromCardUUID());
        if (from != null && card != null) {
            Fight exist = getByFromAndCard(from.getUUID(), card.getUuid(), "pending");
            if (exist != null && exist.isPending())
                return null;
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(card)) {
                fight.setStatus("pending");
                fight = fightRepository.save(fight);
                return fight;
            }
        }
        return null;
    }

    public Fight getByFromAndCard(UUID from, UUID card, String status) {
        return fightRepository.findByFromUserUUIDAndCardUUID(from, card, status);
    }

    public boolean existTransaction(UUID from, UUID card) {
        return getByFromAndCard(from, card, "pending") != null;
    }

    public boolean isValidCancelTransaction(Fight transaction) {
        if (transaction.getTranscationUUID() == null)
            return false;
        Fight valid = fightRepository.findById(transaction.getTranscationUUID()).orElse(null);
        if (valid == null)
            return false;
        if (valid.getFromUserUUID().equals(transaction.getFromUserUUID()) &&
                valid.getFromCardUUID().equals(transaction.getFromCardUUID()))
            return true;
        return false;
    }

    public boolean isValidAcceptTransaction(Fight transaction) {
        Fight valid = fightRepository.findById(transaction.getTranscationUUID()).orElse(null);
        if (valid == null)
            return false;
        if (valid.getFromUserUUID().equals(transaction.getFromUserUUID()) &&
                valid.getFromCardUUID().equals(transaction.getFromCardUUID()) && valid.isPending())
            return true;
        return false;
    }

    public Fight cancelTransaction(Fight transaction) {
        if (!isValidCancelTransaction(transaction))
            return null;
        UserDTO from = Utils.getUser(transaction.getFromUserUUID());
        CardDTO card = Utils.getCard(transaction.getFromCardUUID());
        if (from != null && card != null) {
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(card)) {
                transaction.setStatus("canceled");
                fightRepository.save(transaction);
                fightRepository.delete(transaction);
                return transaction;
            }
        }
        return null;
    }

    public List<Fight> getTransactions() {
        List<Fight> transactions = new ArrayList<Fight>();
        fightRepository.findAll().iterator().forEachRemaining(transactions::add);
        return transactions;
    }

    public Fight acceptFight(Fight fight) {
        if (!isValidAcceptTransaction(fight))
            return null;
        UserDTO to = Utils.getUser(fight.getToUserUUID());
        CardDTO cardTo = Utils.getCard(fight.getToCardUUID());
        UserDTO from = Utils.getUser(fight.getFromUserUUID());
        if (cardTo != null && to != null) {
            Fight valid =  getByFromAndCard(to.getUUID(), cardTo.getUuid(), "pending");
            if (valid == null)
                return null;
            if (valid.getPrice() > to.getBalance())
                return null;
            CardDTO[] cardDTOS = Utils.getOwnerCards(from.getUUID().toString());
            List<CardDTO> cardDTOList = Arrays.asList(cardDTOS);
            if (cardDTOList.contains(cardTo)) {
                fight.setStatus("accepted");
                Utils.changeOwner(cardTo, to);
                Utils.debit(to.getUUID(), valid.getPrice());
                Utils.depot(from.getUUID(), valid.getPrice());
                fightRepository.save(valid);
                fightRepository.delete(valid);
                System.out.println("Fight accepted");
                return fight;
            }
        }
        return null;
    }
}
