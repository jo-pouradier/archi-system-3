package com.sp.controller;

import com.sp.model.Card;
import com.sp.model.CardDTO;
import com.sp.service.CardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


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

    @GetMapping(value = "/getCards", produces = "application/json")
    public ResponseEntity<List<CardDTO>> getCards() {
        List<CardDTO> cardDTOS = cardService.convertToDTO(cardService.getCards());
        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<?> getCard(@PathVariable("uuid") String uuid) {
        if (uuid == null) return new ResponseEntity<>("the uuid can't be null", HttpStatus.BAD_REQUEST);
        Card card = cardService.getCard(UUID.fromString(uuid));
        if (card == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        CardDTO cardDTO = new CardDTO();
        BeanUtils.copyProperties(card, cardDTO);
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/getCardsByOwner", produces = "application/json")
    public ResponseEntity<?> getCardsByOwnerUUID(@CookieValue("user") String cookieUserUuid) {
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

    @GetMapping(value = "/newUserSet")
    public ResponseEntity<?> newUserSet(@CookieValue("user") String cookieUserUuid){
        System.out.println("cookieUserUuid = " + cookieUserUuid);
        // FIXME est ce qu'on a besoin de tout User? On peut juste utiliser le cookie (qui est le user uuid), verifier avec authService si c'est un uuid d'un user.
        if (cookieUserUuid == null) return new ResponseEntity<>("you must be authenticated", HttpStatus.UNAUTHORIZED);
        //fetch user from http://localhost:8083/isUser/{uuid}
        // TODO ceci est un test
//        String uri = "http://localhost:8000/auth/isUser/" + cookieUserUuid;
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
//        System.out.println("response = " + response);
        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://127.0.0.1:8000/auth/isUser"; // or any other uri

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "user=" + cookieUserUuid);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
//        return result.getBody();
        System.out.println("result = " + result);
        cardService.newUserSet(UUID.fromString(cookieUserUuid));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

//    @GetMapping(value = "/newUserSet")
//    public ResponseEntity<String> newUserSet(@CookieValue(value = "user") String cookieUserUuid) {
//        if (cookieUserUuid == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be authenticated");
//        }
//
//        WebClient client = WebClient.create();
//        String uri = "http://127.0.0.1:8000/auth/isUser"; // or any other URI
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.add(HttpHeaders.COOKIE, "user=" + cookieUserUuid);
//
//        Mono<ResponseEntity<String>> responseEntityMono = client.get()
//                .uri(uri)
//                .headers(httpHeaders -> httpHeaders.addAll(headers))
//                .retrieve()
//                .toEntity(String.class);
//
//        return responseEntityMono
//                .flatMap(responseEntity -> {
//                    System.out.println("result = " + responseEntity);
//                    cardService.newUserSet(UUID.fromString(cookieUserUuid));
//                    return Mono.just(ResponseEntity.ok().build());
//                })
//                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
//                .block(); // Blocking for simplicity, consider using non-blocking approach in production
//    }
//@GetMapping(value = "/newUserSet")
//public ResponseEntity<?> newUserSet(@CookieValue(value = "user", required = false) String cookieUserUuid) {
//    if (cookieUserUuid == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be authenticated");
//    }
//
//    WebClient client = WebClient.create();
//    String uri = "http://127.0.0.1:8000/auth/isUser"; // or any other URI
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//    headers.add(HttpHeaders.COOKIE, "user=" + cookieUserUuid);
//
//   return client.get()
//            .uri(uri)
//            .headers(httpHeaders -> httpHeaders.addAll(headers))
//            .retrieve()
//            .bodyToMono(String.class)
//            .flatMap(responseBody -> {
//                if (responseBody != null && !responseBody.isEmpty()) {
//                    List<Card> cards = cardService.newUserSet(UUID.fromString(cookieUserUuid));
//                    System.out.println("cards = " + cards);
//                    cards.forEach(System.out::println);
//                    return Mono.just(ResponseEntity.ok().body(cards));
//                } else {
//                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
//                }
//            })
//            .block(); // Blocking for simplicity, consider using non-blocking approach in production
//}

}
