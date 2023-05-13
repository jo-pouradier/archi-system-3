package com.sp.service;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.repository.CardRepository;
import com.sp.tools.CardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card getCard(UUID uuid){
        return cardRepository.findById(uuid).orElse(null);
    }

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<Card>();
        cardRepository.findAll().iterator().forEachRemaining(cards::add);
        return cards;
    }

    public void newUserSet(User user){
        //Give him 5 random Cards
        List<Card> cards = CardFactory.generateRandomListFromTemplates(5);
        this.setCards(user, cards);
    }

    private void setCards(User user, List<Card> cards) {
        cards.forEach(card -> {
            card.setOwnerUUID(user.getUUID());
            cardRepository.save(card);
        });
    }

    public List<Card> getCardsByOwnerUUID(UUID uuid){
        return cardRepository.findByOwnerUUID(uuid);
    }

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }

    public void deleteCard(UUID uuid){
        cardRepository.deleteById(uuid);
    }

    public void deleteCard(Card card){
        cardRepository.delete(card);
    }

    public void changeOwner(Card card, User to) {
        card.setOwnerUUID(to.getUUID());
        cardRepository.update(card);
    }
}
