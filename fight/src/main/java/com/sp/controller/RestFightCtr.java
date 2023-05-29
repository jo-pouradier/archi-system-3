package com.sp.controller;

import com.sp.FightApp;
import com.sp.model.Fight;
import com.sp.service.FightService;
import fr.dtos.common.card.CardDTO;
import fr.dtos.common.fight.FightDTO;
import fr.dtos.common.market.TransactionDTO;
import fr.dtos.common.utils.EServices;
import fr.dtos.common.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
public class RestFightCtr {

    @Autowired
    private FightService fightService;

    @GetMapping(value = "/getFights", produces = "application/json")
    public ResponseEntity<?> getFights() {
        List<Fight> fights = fightService.getTransactions();
        if (isNull(fights)) return new ResponseEntity<>("Display not allowed", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(fights, HttpStatus.OK);
    }
    @PostMapping(value = "/createFight", produces = "application/json")
    public ResponseEntity<?> createFight(@RequestBody FightDTO fightDTO) {
        if (fightDTO.getPrice()<0) return new ResponseEntity<>("Price must be positive", HttpStatus.FORBIDDEN);
        Fight transaction = new Fight();
        BeanUtils.copyProperties(fightDTO, transaction);
        Fight fight = fightService.createFight(transaction);
        System.out.println(fight);
        if (isNull(fight)) return new ResponseEntity<>("Fight not allowed!", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(fight, HttpStatus.OK);
    }

    @PostMapping(value = "/acceptFight", produces = "application/json")
    public ResponseEntity<?> acceptFight(@RequestBody FightDTO fightDTO) {
        System.out.println(fightDTO);
        Fight fight = new Fight();
        BeanUtils.copyProperties(fightDTO, fight);
        Fight fightTransaction = fightService.acceptFight(fight);
        // TODO fight
        System.out.println(fightTransaction);
        if (isNull(fightTransaction)) return new ResponseEntity<>("Fight not allowed!", HttpStatus.FORBIDDEN);
        // TODO update energy
        Utils.updateEnergy(fightTransaction.getToCardUUID(), 10);
        Utils.updateEnergy(fightTransaction.getFromCardUUID(),10);
        //CardDTO updatedCard = Utils.requestService(EServices.CARD_SERVICE, "updatePrice/", transactionDto, CardDTO.class, HttpMethod.POST);
        return new ResponseEntity<>(fightTransaction, HttpStatus.OK);
    }
}

