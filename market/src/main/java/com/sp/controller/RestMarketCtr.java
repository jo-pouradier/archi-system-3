package com.sp.controller;

import com.sp.model.Transaction;
import com.sp.repository.MarketRepository;
import com.sp.service.MarketService;
import fr.dtos.common.card.CardDTO;
import fr.dtos.common.market.TransactionDTO;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
public class RestMarketCtr {

    /*
    Créer une transaction
    Annuler une transaction
    Accepter une transaction
     */

    @Autowired
    private MarketService market;

    @GetMapping(value = "/displayMarket", produces = "application/json")
    public ResponseEntity<?> displayMarket() {
        List<Transaction> marketTransaction = market.getTransactions();
        if (isNull(marketTransaction)) return new ResponseEntity<>("Display not allowed", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @GetMapping(value = "/getTransactions", produces = "application/json")
    public ResponseEntity<?> getTransactions() {
        List<Transaction> marketTransaction = market.getTransactions().stream().filter(transaction -> transaction.getStatus().equals("pending")).collect(Collectors.toList());
        if (isNull(marketTransaction)) return new ResponseEntity<>("Display not allowed", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @PostMapping(value = "/createTransaction", produces = "application/json")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        if (transactionDTO.getPrice()<0) return new ResponseEntity<>("Price must be positive", HttpStatus.FORBIDDEN);
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        Transaction marketTransaction = market.createTransaction(transaction);
        System.out.println(marketTransaction);
        CardDTO updatedCard = Utils.requestService(EServices.CARD_SERVICE, "updatePrice/", transactionDTO, CardDTO.class, HttpMethod.POST);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Transaction not allowed!", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }


    @PostMapping(value = "/acceptTransaction", produces = "application/json")
    public ResponseEntity<?> acceptTransaction(@RequestBody TransactionDTO transactionDto) {
        System.out.println(transactionDto);
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDto, transaction);
        Transaction marketTransaction = market.acceptTransaction(transaction);
        transactionDto.setPrice(-1);
        System.out.println(marketTransaction);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Transaction not allowed!", HttpStatus.FORBIDDEN);
        CardDTO updatedCard = Utils.requestService(EServices.CARD_SERVICE, "updatePrice/", transactionDto, CardDTO.class, HttpMethod.POST);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @PostMapping(value = "/cancelTransaction", produces = "application/json")
    public ResponseEntity<?> cancelTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = market.getByFromAndCard(transactionDTO.getFromUserUUID(), transactionDTO.getCardUUID(), "pending");
        Transaction marketTransaction = market.cancelTransaction(transaction);
        transactionDTO.setPrice(-1);
        CardDTO updatedCard = Utils.requestService(EServices.CARD_SERVICE, "updatePrice/", transactionDTO, CardDTO.class, HttpMethod.POST);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Not allowed!", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }
}

