package com.example.app_tiendamovil.Collection.message;

public class DataInfoChat {

    private String nickname;
    private String message ;
    private String date;
    private String avatar;

    public  DataInfoChat(){

    }
    public DataInfoChat(String nickname, String message, String date, String avatar) {
        this.nickname = nickname;
        this.message = message;
        this.date = date;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
