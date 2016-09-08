package com.serkankaplan.common.messagepojo;

/**
 * POJO for event messages
 * Created by serkan on 07/09/16.
 */
public class Event {

    private String username;
    private String content;

    public Event(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
