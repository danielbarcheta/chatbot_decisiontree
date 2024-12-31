package com.botchat.botchat.dto;

import java.util.Map;

public class ChatbotResponse {

    private String message;
    private Map<Integer, String> options;
    private boolean isError;
    private String inputPromptMessage;
    private String sessionId;  // Novo campo para o UUID da sessão

    public ChatbotResponse(String message, Map<Integer, String> options, boolean isError, String inputPromptMessage, String sessionId) {
        this.message = message;
        this.options = options;
        this.isError = isError;
        this.inputPromptMessage = inputPromptMessage;
        this.sessionId = sessionId;  // Inicializa o sessionId
    }

    // Getters e Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Integer, String> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, String> options) {
        this.options = options;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getInputPromptMessage() {
        return inputPromptMessage;
    }

    public void setInputPromptMessage(String inputPromptMessage) {
        this.inputPromptMessage = inputPromptMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
