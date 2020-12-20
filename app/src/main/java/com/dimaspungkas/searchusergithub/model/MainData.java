package com.dimaspungkas.searchusergithub.model;

import com.google.gson.annotations.SerializedName;

public class MainData {

    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatar_url;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getAvatar_url() {
            return avatar_url;
        }
}
