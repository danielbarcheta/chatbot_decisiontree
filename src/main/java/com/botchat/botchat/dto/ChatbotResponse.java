package com.botchat.botchat.dto;


import java.util.Map;

public class ChatbotResponse {
    private String message;
    private Map<Integer, String> options;
    private boolean isInputExpected;
    private String inputPromptMessage;

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

    public boolean isInputExpected() {
        return isInputExpected;
    }

    public void setInputExpected(boolean inputExpected) {
        isInputExpected = inputExpected;
    }

    public String getInputPromptMessage() {
        return inputPromptMessage;
    }

    public void setInputPromptMessage(String inputPromptMessage) {
        this.inputPromptMessage = inputPromptMessage;
    }

    public ChatbotResponse(String message, Map<Integer, String> options, boolean isInputExpected, String inputPromptMessage) {
        this.message = message;
        this.options = options;
        this.isInputExpected = isInputExpected;
        this.inputPromptMessage = inputPromptMessage;
    }
}
