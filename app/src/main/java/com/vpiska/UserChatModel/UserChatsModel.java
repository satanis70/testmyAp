package com.vpiska.UserChatModel;

import com.google.firebase.database.DatabaseReference;

public class UserChatsModel {

    String userImg;
    String autor;
    String lastMessage;
    String Time;




    public UserChatsModel(String autor, String userImg, String Time, String lastMessage) {
        this.userImg = userImg;
        this.autor = autor;
        this.Time = Time;
        this.lastMessage = lastMessage;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return autor;
    }

    public void setUserName(String userName) {
        this.autor = autor;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}
