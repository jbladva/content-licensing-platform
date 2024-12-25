package com.clp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailRequest {
    private String name;
    private String to;
    private String from;
    private String subject;
}
