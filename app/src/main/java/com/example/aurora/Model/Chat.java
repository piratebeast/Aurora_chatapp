package com.example.aurora.Model;

public class Chat {
    String chatId,message,receiver,sender;

    public Chat(String chatId, String message, String receiver, String sender) {
        this.chatId = chatId;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Chat() {
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}
