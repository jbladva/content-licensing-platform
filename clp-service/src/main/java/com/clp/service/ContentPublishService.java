package com.clp.service;

import org.springframework.stereotype.Component;

@Component
public interface ContentPublishService {

    String publishContentRequest(Long contentId, Long publisherId);
}
