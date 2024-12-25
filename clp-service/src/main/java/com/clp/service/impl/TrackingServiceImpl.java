package com.clp.service.impl;

import com.clp.dto.TrackDto;
import com.clp.entity.PublisherContent;
import com.clp.entity.User;
import com.clp.repository.PublisherContentRepository;
import com.clp.security.UserContextHolder;
import com.clp.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrackingServiceImpl implements TrackingService {

    private final PublisherContentRepository publisherContentRepository;

    @Autowired
    TrackingServiceImpl(PublisherContentRepository publisherContentRepository) {
        this.publisherContentRepository = publisherContentRepository;
    }

    @Override
    public String calculateTrackAmount(Long contentId, TrackDto dto) {
        User user = UserContextHolder.getUser();
        PublisherContent publisherContent = publisherContentRepository
                .findByContent_IdAndPublisher_User_Id(contentId, user.getId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Content not found with respective publisher")
                );

        if (dto != null){
            publisherContent.setVisitCount(dto.getVisitCount());
            publisherContent.setDownloadCont(dto.getDownloadCount());
            publisherContent.setAmount(calculate(dto.getVisitCount(), dto.getDownloadCount()));
            publisherContentRepository.save(publisherContent);
            return "Amount calculated: " + publisherContent.getAmount();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dto should not be null");
    }

    private Double calculate(Long visitCount, Long downloadCount) {
        return visitCount*0.5 + downloadCount*1.5;
    }
}
