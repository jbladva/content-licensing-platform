package com.clp.enums;

public enum Actions {
    ACCEPT("accept"),
    REJECT("reject"),
    PUBLISHED("published"),
    UNPUBLISHED("unpublished");


    private final String action;
    Actions(String action) {
        this.action=action;
    }
    public String getAction(){
        return action;
    }
}
