package com.botchat.botchat.controller;


import com.botchat.botchat.dto.ChatbotResponse;
import com.botchat.botchat.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/start")
    public String startSession(@RequestParam String userName) {
        return chatbotService.startSession(userName);
    }

    @PostMapping("/process/{uuid}")
    public ChatbotResponse processUserInput(
            @PathVariable String uuid,
            @RequestBody String userInput) {
        return chatbotService.processUserInput(uuid, userInput);
    }
}
