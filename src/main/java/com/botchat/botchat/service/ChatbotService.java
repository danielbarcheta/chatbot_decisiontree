package com.botchat.botchat.service;

import com.botchat.botchat.domain.ChatSession;
import com.botchat.botchat.domain.Node;
import com.botchat.botchat.dto.ChatbotResponse;
import com.botchat.botchat.repository.ChatSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    private static Node startNode;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    static {
        initializeConversationFlow();
    }

    private static void initializeConversationFlow() {
        startNode = new Node("Bem-vindo ao nosso chatbot! Escolha uma opção:\n1. Opção 1\n2. Opção 2\n3. Opção 3\n4. Opção 4\n5. Opção 5");

        Node option1Node = new Node("Você escolheu a Opção 1! Vamos prosseguir.");
        Node option2Node = new Node("Você escolheu a Opção 2! Por favor, forneça um CPF.");
        Node option3Node = new Node("Você escolheu a Opção 3! Por favor, digite seu nome.");
        Node option4Node = new Node("Você escolheu a Opção 4! Informe sua idade.");
        Node option5Node = new Node("Você escolheu a Opção 5! Vamos finalizar a conversa.");

        startNode.addOption(1, option1Node);
        startNode.addOption(2, option2Node);
        startNode.addOption(3, option3Node);
        startNode.addOption(4, option4Node);
        startNode.addOption(5, option5Node);

        option2Node.setInputExpected(true, "Informe seu CPF:", input -> input.matches("\\d{11}"));
        option3Node.setInputExpected(true, "Informe seu nome:", input -> input.length() > 0);
    }

    @Transactional
    public String startSession(String userName) {
        String uuid = java.util.UUID.randomUUID().toString();
        ChatSession session = new ChatSession();
        session.setUuid(uuid);
        session.setUserName(userName);
        session.setCurrentNodeId(0);
        chatSessionRepository.save(session);
        return uuid;
    }

    public ChatbotResponse processUserInput(String uuid, String userInput) {
        ChatSession session = chatSessionRepository.findById(uuid).orElse(null);

        if (session != null) {
            Node currentNode = startNode;

            for (int i = 0; i < session.getCurrentNodeId(); i++) {
                currentNode = currentNode.getOptions().get(i);
            }

            if (currentNode.isInputExpected()) {
                if (currentNode.getInputValidator().test(userInput)) {
                    session.setUserInput(userInput);
                    session.setCurrentNodeId(session.getCurrentNodeId() + 1);
                    chatSessionRepository.save(session);

                    // Retorna uma resposta estruturada em JSON
                    return new ChatbotResponse(
                            currentNode.getMessage(),
                            new HashMap<>(), // Caso haja opções, podemos preenchê-las aqui
                            false,
                            currentNode.getInputPromptMessage()
                    );
                } else {
                    return new ChatbotResponse(
                            "Entrada inválida! " + currentNode.getInputPromptMessage(),
                            new HashMap<>(),
                            true,
                            currentNode.getInputPromptMessage()
                    );
                }
            }

            Integer option = Integer.parseInt(userInput);
            if (currentNode.getOptions().containsKey(option)) {
                session.setCurrentNodeId(session.getCurrentNodeId() + 1); // Move para o próximo nó
                chatSessionRepository.save(session);

                // Monta o JSON com as opções para o front
                Map<Integer, String> options = new HashMap<>();
                for (Map.Entry<Integer, Node> entry : currentNode.getOptions().entrySet()) {
                    options.put(entry.getKey(), entry.getValue().getMessage());
                }

                return new ChatbotResponse(
                        currentNode.getMessage(),
                        options,
                        false,
                        null
                );
            }

            return new ChatbotResponse("Opção inválida! Tente novamente.", new HashMap<>(), false, null);
        }

        return new ChatbotResponse("Sessão não encontrada.", new HashMap<>(), false, null);
    }
}
