package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.websocket.WebSocketMessage;
import java.security.Principal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/chat")
    public WebSocketMessage chat(WebSocketMessage message, Principal principal) {
        return WebSocketMessage.of("안녕하세요 " + principal.getName() + "! " + message.content() + "라고 하셨나요?");
    }
}
