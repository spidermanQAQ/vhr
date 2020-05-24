package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    HrService hrService;

    @RequestMapping("/hrs")
    public List<Hr>getAllHrsExcludeCurrentHr(){
        return hrService.getAllHrsExcludeCurrentHr();
    }

}
