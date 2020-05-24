package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.ChatMsg;
import org.javaboy.vhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@Controller
public class WsController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    //principal获取发送消息的用户的信息,chatMsg发送过来的消息
    @MessageMapping("/ws/chat")
    public void handleMessage(Authentication authentication, ChatMsg chatMsg){
        Hr hr=((Hr) authentication.getPrincipal());
        chatMsg.setFrom(hr.getUsername());
        chatMsg.setFromNickName(hr.getName());
        chatMsg.setDate(new Date());
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }
}
