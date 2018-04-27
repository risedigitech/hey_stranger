package com.houseofapps.heystranger;

/**
 * Created by abc on 20/04/2018.
 */

class ChatMessage {

    String chat_username;
    String chat_msg;
    String chat_timestamp;
    String data_type;
    String get_token;

    public String getChat_username() {
        return chat_username;
    }

    public void setChat_username(String chat_username) {
        this.chat_username = chat_username;
    }

    public String getChat_msg() {
        return chat_msg;
    }

    public void setChat_msg(String chat_msg) {
        this.chat_msg = chat_msg;
    }

    public String getChat_timestamp() {
        return chat_timestamp;
    }

    public void setChat_timestamp(String chat_timestamp) {
        this.chat_timestamp = chat_timestamp;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getGet_token() {
        return get_token;
    }

    public void setGet_token(String get_token) {
        this.get_token = get_token;
    }

    public ChatMessage(String chat_username, String chat_msg, String chat_timestamp, String data_type, String get_token) {

        this.chat_username = chat_username;
        this.chat_msg = chat_msg;
        this.chat_timestamp = chat_timestamp;
        this.data_type = data_type;
        this.get_token = get_token;
    }
}
