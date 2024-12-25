package com.clp.enums;

public enum Status {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected"),
    PUBLISHED("published"),
    UNPUBLISHED("unpublished");

    private final String status;


    Status(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
