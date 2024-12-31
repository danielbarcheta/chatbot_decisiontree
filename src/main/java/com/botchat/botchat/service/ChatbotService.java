package com.botchat.botchat.service;

import com.botchat.botchat.domain.ChatSession;
import com.botchat.botchat.domain.Node;
import com.botchat.botchat.dto.ChatbotResponse;
import com.botchat.botchat.dto.StartSessionDTO;
import com.botchat.botchat.dto.UserInputDTO;
import com.botchat.botchat.repository.ChatSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatbotService {

    private static Node startNode;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    static {
        initializeConversationFlow();
    }

    private static void initializeConversationFlow() {
        startNode = new Node("Bem-vindo ao nosso chatbot! Sobre o que gostaria de falar hoje?");

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
    public ChatbotResponse startSession(StartSessionDTO startSessionDTO) {
        String userName = startSessionDTO.getUserName();
        String uuid = java.util.UUID.randomUUID().toString();

        ChatSession session = new ChatSession();
        session.setUuid(uuid);
        session.setUserName(userName);
        session.setCurrentNodeId(0);
        chatSessionRepository.save(session);

        Map<Integer, String> options = new HashMap<>();
        for (Map.Entry<Integer, Node> entry : startNode.getOptions().entrySet()) {
            options.put(entry.getKey(), entry.getValue().getMessage());
        }

        return new ChatbotResponse(
                "Bem-vindo, " + userName + "! O que gostaria de fazer agora?",
                options,
                false,
                null,
                uuid
        );
    }

    public ChatbotResponse processUserInput(String uuid, UserInputDTO userInputDTO) {
        String userInput = userInputDTO.getUserInput();
        Optional<ChatSession> optionalSession = chatSessionRepository.findById(uuid);

        if (optionalSession.isPresent()) {
            ChatSession session = optionalSession.get();
            Node currentNode = startNode;

            // Navegar até o nó atual baseado no currentNodeId
            for (int i = 0; i < session.getCurrentNodeId(); i++) {
                currentNode = currentNode.getOptions().get(i);
            }

            if (currentNode.isInputExpected()) {
                if (currentNode.getInputValidator().test(userInput)) {
                    session.setUserInput(userInput);
                    session.setCurrentNodeId(session.getCurrentNodeId() + 1);
                    chatSessionRepository.save(session);

                    return new ChatbotResponse(
                            currentNode.getMessage(),
                            new HashMap<>(),
                            false,
                            currentNode.getInputPromptMessage(),
                            uuid
                    );
                } else {
                    return new ChatbotResponse(
                            "Entrada inválida! " + currentNode.getInputPromptMessage(),
                            new HashMap<>(),
                            true,
                            currentNode.getInputPromptMessage(),
                            uuid
                    );
                }
            }

            try {
                Integer option = Integer.parseInt(userInput);
                if (currentNode.getOptions().containsKey(option)) {
                    // Atualiza o currentNodeId para o próximo nó após a escolha
                    Node nextNode = currentNode.getOptions().get(option);
                    session.setCurrentNodeId(session.getCurrentNodeId() + 1); // Atualiza o nó atual
                    chatSessionRepository.save(session);

                    // Monta as opções para o próximo passo
                    Map<Integer, String> options = new HashMap<>();
                    for (Map.Entry<Integer, Node> entry : nextNode.getOptions().entrySet()) {
                        options.put(entry.getKey(), entry.getValue().getMessage());
                    }

                    return new ChatbotResponse(
                            nextNode.getMessage(),
                            options,
                            false,
                            null,
                            uuid
                    );
                }
            } catch (NumberFormatException e) {
                return new ChatbotResponse("Opção inválida! Tente novamente.", new HashMap<>(), false, null, uuid);
            }
        }

        return new ChatbotResponse("Sessão não encontrada.", new HashMap<>(), false, null, uuid);
    }
}
