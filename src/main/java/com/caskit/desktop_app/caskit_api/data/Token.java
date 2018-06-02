package com.caskit.desktop_app.caskit_api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.caskit.desktop_app.utils.Jsonable;

public class Token implements Jsonable {

    @JsonProperty("username")
    private String username;

    @JsonProperty("token")
    private String token;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("date")
    private String date;

    @JsonProperty("ttl")
    private long ttl;

    public Token(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
