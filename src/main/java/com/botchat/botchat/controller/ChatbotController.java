package com.botchat.botchat.controller;

import com.botchat.botchat.service.ChatbotService;
import com.botchat.botchat.dto.ChatbotResponse;
import com.botchat.botchat.dto.StartSessionDTO;
import com.botchat.botchat.dto.UserInputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/start")
    public ChatbotResponse startSession(@RequestBody StartSessionDTO startSessionDTO) {
        return chatbotService.startSession(startSessionDTO);
    }

    @PostMapping("/process/{uuid}")
    public ChatbotResponse processUserInput(
            @PathVariable String uuid,
            @RequestBody UserInputDTO userInputDTO) {

        return chatbotService.processUserInput(uuid, userInputDTO);
    }
}
