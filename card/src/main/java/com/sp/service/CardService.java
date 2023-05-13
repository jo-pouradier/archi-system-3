package com.sp.service;

import com.sp.model.Card;
import com.sp.model.CardDTO;
import com.sp.model.User;
import com.sp.repository.CardRepository;
import com.sp.tools.CardFactory;
import org.springframework.beans.BeanUtils;
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
        List<Card> cards = new ArrayList<>();
        cardRepository.findAll().iterator().forEachRemaining(cards::add);
        return cards;
    }

    public boolean exists(UUID uuid){
        return cardRepository.existsById(uuid);
    }

    public boolean isOwner(Card card, User user){
        return card.getOwnerUUID().equals(user.getUUID());
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

    public void saveCard(Card card){
        cardRepository.save(card);
    }

    public Card deleteCard(UUID uuid){
        Card card = cardRepository.findById(uuid).orElse(null);
        cardRepository.deleteById(uuid);
        return card;
    }

    public void deleteCard(Card card){
        cardRepository.delete(card);
    }

    public Card changeOwner(Card card, User to) {
        card.setOwnerUUID(to.getUUID());
        return cardRepository.update(card);
    }

    public List<CardDTO> convertToDTO(List<Card> cards){
        List<CardDTO> cardDTOs = new ArrayList<>();
        cards.forEach(card -> {
            CardDTO cardDTO = new CardDTO();
            BeanUtils.copyProperties(card, cardDTO);
            cardDTO.setUuid(card.getUuid().toString());
            cardDTOs.add(cardDTO);
        });
        return cardDTOs;
    }
}
