package com.sp.service;

import com.sp.model.Card;
import fr.dtos.common.card.CardDTO;
import com.sp.repository.CardRepository;
import com.sp.tools.CardFactory;
import fr.dtos.common.user.UserDTO;
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

    public boolean isOwner(Card card, UserDTO user){
        return card.getOwnerUUID().equals(user.getUUID());
    }

    public List<Card> newUserSet(UserDTO user){
        //Give him 5 random Cards
        List<Card> cards = CardFactory.generateRandomListFromTemplates(5);
        this.setCards(user, cards);
        return cards;
    }

    public List<Card> newUserSet(UUID uuid){
        //Give him 5 random Cards
        List<Card> cards = CardFactory.generateRandomListFromTemplates(5);
        this.setCards(uuid, cards);
        return cards;
    }

    private void setCards(UserDTO user, List<Card> cards) {
        cards.forEach(card -> {
            card.setOwnerUUID(user.getUUID());
            cardRepository.save(card);
        });
    }

    private void setCards(UUID uuid, List<Card> cards) {
        cards.forEach(card -> {
            card.setOwnerUUID(uuid);
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

    public Card changeOwner(Card card, UserDTO to) {
        card.setOwnerUUID(to.getUUID());
        return cardRepository.update(card);
    }

    public List<CardDTO> convertToDTO(List<Card> cards){
        List<CardDTO> cardDTOs = new ArrayList<>();
        cards.forEach(card -> {
            CardDTO cardDTO = new CardDTO();
            BeanUtils.copyProperties(card, cardDTO);
            cardDTO.setUuid(card.getUuid());
            cardDTOs.add(cardDTO);
        });
        return cardDTOs;
    }
}
