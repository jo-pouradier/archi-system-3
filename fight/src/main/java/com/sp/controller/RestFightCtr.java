package com.sp.controller;

import com.sp.service.FightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestFightCtr {

    /*
    Créer une transaction
    Annuler une transaction
    Accepter une transaction
     */

    @Autowired
    private FightService fightService;

}

