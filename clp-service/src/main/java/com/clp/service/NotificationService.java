package com.clp.service;

import com.clp.dto.MailRequest;

import java.util.Map;

public interface NotificationService {

    void sendActionNotification(MailRequest mailRequest, Map<String,String> model, String s);
}
