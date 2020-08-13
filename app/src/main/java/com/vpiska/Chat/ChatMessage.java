package com.vpiska.Chat;

import java.util.Date;

public class ChatMessage  {

    private String textMessage;
    private String autor;
    private long timeMessage;



    private String receiver;
    String userImage;


    public ChatMessage(String textMessage, String autor, String receiver) {
        this.textMessage = textMessage;
        this.autor = autor;
        this.receiver = receiver;
        timeMessage = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
