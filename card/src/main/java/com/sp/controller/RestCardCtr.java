package com.sp.controller;

import com.sp.model.Card;
import com.sp.model.CardDTO;
import com.sp.service.CardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins={"http://localhost:8080", "http://localhost:8081", "http://localhost:8082", "http://localhost:8083", "http://localhost:8084", "http://localhost:8085"}, allowedHeaders = "*")
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

    @GetMapping(value = "/getCardsByOwner", produces = "application/json")
    public ResponseEntity<?> getCardsByOwnerUUID(@CookieValue String cookieUserUuid) {
        if (cookieUserUuid == null) return new ResponseEntity<>("You must be authenticated", HttpStatus.UNAUTHORIZED);
        List<Card> cards = cardService.getCardsByOwnerUUID(UUID.fromString(cookieUserUuid));
        if (cards == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        List<CardDTO> cardDTOS = cardService.convertToDTO(cards);
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }

    @PostMapping (value = "/addCard", produces = "application/json")
    public ResponseEntity<?> addCard(@RequestBody CardDTO cardDTO, @CookieValue("user") String cookieUserUuid){
        if (cardDTO == null) return new ResponseEntity<>("Your body is null", HttpStatus.BAD_REQUEST);
        if (cookieUserUuid == null) return new ResponseEntity<>("You need to be authenticated", HttpStatus.UNAUTHORIZED);
        Card card = new Card();
        BeanUtils.copyProperties(cardDTO, card);
        card.setOwnerUUID(UUID.fromString(cookieUserUuid));
        cardService.saveCard(card);
        cardDTO.setUuid(card.getUuid());
        cardDTO.setOwnerUUID(card.getOwnerUUID());
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> deleteCard(@PathVariable("uuid") String uuidString, @CookieValue("user") String cookieUserUuid){
        System.out.println("uuidString = " + uuidString);
        System.out.println("cookieUserUuid = " + cookieUserUuid);
        if (cookieUserUuid == null) return new ResponseEntity<>(null ,HttpStatus.UNAUTHORIZED);
        UUID uuid = UUID.fromString(uuidString);
        if (!cardService.exists(uuid)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Card card = cardService.getCard(uuid);
        System.out.println("card.getOwnerUUID() = " + card.getOwnerUUID());
        if (card.getOwnerUUID().compareTo(UUID.fromString(cookieUserUuid)) != 0) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Card cardDeleted = cardService.deleteCard(uuid);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(cardDeleted, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/newUserSet")
    public ResponseEntity<?> newUserSet(@CookieValue("user") String cookieUserUuid){
        // FIXME est ce qu'on a besoin de tout User? On peut juste utiliser le cookie (qui est le user uuid), verifier avec authService si c'est un uuid d'un user.
        if (cookieUserUuid == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        //fetch user from http://localhost:8083/isUser/{uuid}

//        cardService.newUserSet();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
