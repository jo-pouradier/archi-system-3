package com.sp.controller;

import com.sp.model.Card;
import fr.dtos.common.card.CardDTO;
import com.sp.service.CardService;
import fr.dtos.common.fight.FightDTO;
import fr.dtos.common.market.TransactionDTO;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.apache.tomcat.jni.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class RestCardCtr {

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/", produces = "application/json")
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }

    @GetMapping(value = "getCards", produces = "application/json")
    public ResponseEntity<List<CardDTO>> getCards() {
        List<CardDTO> cardDTOS = cardService.convertToDTO(cardService.getCards());
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "getCard/{uuid}", produces = "application/json")
    public ResponseEntity<?> getCard(@PathVariable("uuid") String uuid) {
        System.out.println("getCard " + uuid);
        if (uuid == null || UUID.fromString(uuid) == null) return new ResponseEntity<>("the uuid can't be null", HttpStatus.BAD_REQUEST);
        Card card = cardService.getCard(UUID.fromString(uuid));
        System.out.println("   card: " + card);
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        System.out.println("   return: " + cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/getCardsByOwner/{uuid}", produces = "application/json")
    public ResponseEntity<?> getCardsByOwnerUUID(@PathVariable("uuid") String uuidString, @CookieValue("user") String cookieUserUuid) {
//        System.out.println("getCardsByOwner");
//        System.out.println("uuid = " + uuidString);
//        System.out.println("cookieUserUuid = " + cookieUserUuid);
        if (cookieUserUuid == null) return new ResponseEntity<>("You must be authenticated", HttpStatus.UNAUTHORIZED);
        List<Card> cards = cardService.getCardsByOwnerUUID(UUID.fromString(uuidString));
        if (cards == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        List<CardDTO> cardDTOS = cardService.convertToDTO(cards);
        //System.out.println(Arrays.toString(cardDTOS.toArray()));
        //System.out.println(Arrays.toString(cardService.getCards().toArray()));
        return new ResponseEntity<>(cardDTOS.toArray(), HttpStatus.OK);
    }

    @PostMapping (value = "/addCard", produces = "application/json")
    public ResponseEntity<?> addCard(@RequestBody CardDTO cardDTO, @CookieValue("user") String cookieUserUuid){
        if (cardDTO == null) return new ResponseEntity<>("Your body is null", HttpStatus.BAD_REQUEST);
        if (cookieUserUuid == null) return new ResponseEntity<>("You need to be authenticated", HttpStatus.UNAUTHORIZED);
//
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
        if (cookieUserUuid == null) return new ResponseEntity<>(null ,HttpStatus.UNAUTHORIZED);
        UUID uuid = UUID.fromString(uuidString);
        if (!cardService.exists(uuid)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Card card = cardService.getCard(uuid);
        if (card.getOwnerUUID().compareTo(UUID.fromString(cookieUserUuid)) != 0) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Card cardDeleted = cardService.deleteCard(uuid);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(cardDeleted, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/newUserSet/{uuid}")
    public ResponseEntity<?> newUserSet(@PathVariable("uuid") String uuidString, @CookieValue("user") String cookieUserUuid){
        //System.out.println("cookieUserUuid = " + cookieUserUuid);
        //System.out.println("uuidString = " + uuidString);
        if (cookieUserUuid == null || !Utils.getApiSuperAdminKey().equals(cookieUserUuid)) return new ResponseEntity<>("you must be authenticated", HttpStatus.UNAUTHORIZED);
        UserDTO userDTO = Utils.requestService(EServices.USER_SERVICE,"getUser/"+uuidString, null, UserDTO.class);
        //System.out.println("userDTO = " + userDTO);
        if(userDTO == null) return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
//        return result.getBody();
        List<Card> cards = cardService.newUserSet(UUID.fromString(uuidString));
        List<CardDTO> cardDTOS = cardService.convertToDTO(cards);
        //System.out.println("Created new set" + cardService.getCardsByOwnerUUID(UUID.fromString(uuidString)));
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }

    @PostMapping(value = "/updatePrice", produces = "application/json")
    public ResponseEntity<?> updatePrice(@RequestBody TransactionDTO transactionDTO, @CookieValue("user") String cookieUserUuid){
        System.out.println("transactionDTO = " + transactionDTO + "\ncookieUserUuid = " + cookieUserUuid);
        if (transactionDTO == null) return new ResponseEntity<>("Your body is null", HttpStatus.BAD_REQUEST);
        if (cookieUserUuid == null && !Utils.isUserKey(cookieUserUuid)) return new ResponseEntity<>("You need to be authenticated", HttpStatus.UNAUTHORIZED);
        Card card = cardService.getCard(transactionDTO.getCardUUID());
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        card.setPrice(transactionDTO.getPrice());
        cardService.saveCard(card);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/updateEnergy", produces = "application/json")
    public ResponseEntity<?> updateEnergy(@RequestParam("cardUUID") String cardUUID, @RequestParam("energyAmount") int energyAmount ,@CookieValue("user") String cookieUserUuid){
        System.out.println("cardUUID = " + cardUUID + "\ncookieUserUuid = " + cookieUserUuid);
        if (cardUUID == null || UUID.fromString(cardUUID) == null) return new ResponseEntity<>("Your body is null", HttpStatus.BAD_REQUEST);
        if (cookieUserUuid == null && !Utils.isUserKey(cookieUserUuid)) return new ResponseEntity<>("You need to be authenticated", HttpStatus.UNAUTHORIZED);
        // TODO update energy of 2 cards
        Card card = cardService.getCard(UUID.fromString(cardUUID));
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        card.setEnergy(energyAmount);
        cardService.saveCard(card);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/changeOwner", produces = "application/json")
    public ResponseEntity<?> changeOwner(@RequestParam("cardUUID") String cardUUIDString, @RequestParam("newOwnerUUID") String newOwnerUUIDString, @CookieValue("user") String cookieUserUuid){
        System.out.println("cardUUIDString = " + cardUUIDString + "\nnewOwnerUUIDString = " + newOwnerUUIDString + "\ncookieUserUuid = " + cookieUserUuid);
        if (cardUUIDString == null || newOwnerUUIDString == null) return new ResponseEntity<>("Your body is null", HttpStatus.BAD_REQUEST);
        if (cookieUserUuid == null && !Utils.isUserKey(cookieUserUuid)) return new ResponseEntity<>("You need to be authenticated", HttpStatus.UNAUTHORIZED);
        Card card = cardService.getCard(UUID.fromString(cardUUIDString));
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        card.setOwnerUUID(UUID.fromString(newOwnerUUIDString));
        UserDTO userDTO = Utils.requestService(EServices.USER_SERVICE,"getUser/"+newOwnerUUIDString, null, UserDTO.class);
        cardService.changeOwner(card, userDTO);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

}
