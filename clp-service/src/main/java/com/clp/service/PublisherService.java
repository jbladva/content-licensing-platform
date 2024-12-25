package com.clp.service;

import com.clp.dto.PublisherContentDto;
import com.clp.dto.PublisherDto;
import com.clp.enums.Status;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PublisherService {

    PublisherDto createPublisherProfile(PublisherDto dto);
    PublisherDto getPublisherProfile();
    List<PublisherDto> getPublishers();
    String acceptOrRejectContent(Long contentId, String action);
    List<PublisherContentDto> getAllContent();
    PublisherContentDto getContent(Long ContentId);
    String publishContent(Long ContentId, UUID licenseKey, Status status);
}
