package com.vpiska.Chat;

import java.util.Date;

public class ChatMessage  {

    private String textMessage;
    private String autor;
    private long timeMessage;
    String userImage;

    public ChatMessage(String textMessage, String autor) {
        this.textMessage = textMessage;
        this.autor = autor;

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
}
