package com.botchat.botchat.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Node {
    private String message;
    private Map<Integer, Node> options = new HashMap<>();
    private boolean isInputExpected = false;
    private Predicate<String> inputValidator;
    private String inputPromptMessage;

    public Node(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Integer, Node> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, Node> options) {
        this.options = options;
    }

    public boolean isInputExpected() {
        return isInputExpected;
    }

    public void setInputExpected(boolean isInputExpected) {
        this.isInputExpected = isInputExpected;
    }

    public Predicate<String> getInputValidator() {
        return inputValidator;
    }

    public void setInputValidator(Predicate<String> inputValidator) {
        this.inputValidator = inputValidator;
    }

    public String getInputPromptMessage() {
        return inputPromptMessage;
    }

    public void setInputPromptMessage(String inputPromptMessage) {
        this.inputPromptMessage = inputPromptMessage;
    }

    public void addOption(int optionNumber, Node nextNode) {
        options.put(optionNumber, nextNode);
    }

    public void setInputExpected(boolean isInputExpected, String inputPromptMessage, Predicate<String> inputValidator) {
        this.isInputExpected = isInputExpected;
        this.inputPromptMessage = inputPromptMessage;
        this.inputValidator = inputValidator;
    }
}
