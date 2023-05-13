package com.sp.controller;

import com.sp.model.Card;
import com.sp.model.CardDTO;
import com.sp.model.uuidDTO;
import com.sp.service.CardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("card")
public class RestCardCtr {

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/", produces = "application/json")
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }

    @GetMapping(value = "/getCards", produces = "application/json")
    public ResponseEntity<List<CardDTO>> getCards() {
        List<CardDTO> cardDTOS = cardService.convertToDTO(cardService.getCards());
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<CardDTO> getCard(@PathVariable("uuid") String uuid) {
        Card card = cardService.getCard(UUID.fromString(uuid));
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/getCardsByOwnerUUID/{uuid}", produces = "application/json")
    public ResponseEntity<List<CardDTO>> getCardsByOwnerUUID(@PathVariable("uuid") String uuid) {
        List<Card> cards = cardService.getCardsByOwnerUUID(UUID.fromString(uuid));
        if (cards == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        List<CardDTO> cardDTOS = cardService.convertToDTO(cards);
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }

    @PostMapping (value = "/addCard", produces = "application/json")
    public ResponseEntity<CardDTO> addCard(@RequestBody CardDTO cardDTO){
        if (cardDTO == null) return null;
        Card card = new Card();
        BeanUtils.copyProperties(cardDTO, card);
        cardService.saveCard(card);
        cardDTO.setUuid(String.valueOf(card.getUuid()));
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    // j'ai seulement mis par uuid parce que la fonction de delete par Card sert un peu à rien dcp
    @DeleteMapping(value = "/deleteCard", produces = "application/json")
    public ResponseEntity<CardDTO> deleteCard(@RequestBody uuidDTO uuidDTO){
        if (uuidDTO == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        UUID uuid = uuidDTO.getUuid();
        if (!cardService.exists(uuid)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Card cardDeleted = cardService.deleteCard(uuid);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(cardDeleted, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }
}
