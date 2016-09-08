package com.serkankaplan.common.messagepojo;

/**
 * User Client login object
 * Created by serkan on 07/09/16.
 */
public class User {

    public User(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
