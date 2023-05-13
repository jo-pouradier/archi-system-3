package com.sp.controller;

import com.sp.model.Card;
import com.sp.model.Transaction;
import com.sp.repository.MarketRepository;
import com.sp.service.CardService;
import com.sp.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("market")
public class RestMarketCtr {

    /*
    Créer une transaction
    Annuler une transaction
    Accepter une transaction
     */

    @Autowired
    private MarketService market;

    @Autowired
    private MarketRepository marketrepo;

    @GetMapping(value = "/displayMarket", produces = "application/json")
    public ResponseEntity<?> displayMarket() {
        List<Transaction> marketTransaction = market.getTransactions();
        if(isNull(marketTransaction)) return new ResponseEntity<>("Display not allowed", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @PostMapping(value = "/createTransaction", produces = "application/json")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction){
        System.out.println(transaction);
        Transaction marketTransaction = market.createTransaction(transaction);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Transaction not allowed!",HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @PostMapping(value = "/acceptTransaction", produces = "application/json")
    public ResponseEntity<?> acceptTransaction(@RequestBody Transaction transaction){
        Transaction marketTransaction = market.acceptTransaction(transaction);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Transaction not allowed!",HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(marketTransaction, HttpStatus.OK);
    }

    @PostMapping(value= "/cancelTransaction", produces = "application/json")
    public ResponseEntity<?> cancelTransaction(@RequestBody Transaction transaction){
        Transaction marketTransaction = market.cancelTransaction(transaction);
        if (isNull(marketTransaction)) return new ResponseEntity<>("Not allowed!", HttpStatus.OK);
        return new ResponseEntity<>(marketTransaction,HttpStatus.FORBIDDEN);
    }
}

