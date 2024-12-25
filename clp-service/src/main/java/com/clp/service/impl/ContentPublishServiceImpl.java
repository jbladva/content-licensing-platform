package com.clp.service.impl;

import com.clp.entity.Content;
import com.clp.entity.Publisher;
import com.clp.entity.PublisherContent;
import com.clp.entity.User;
import com.clp.enums.Role;
import com.clp.enums.Status;
import com.clp.repository.ContentRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.repository.PublisherRepository;
import com.clp.security.UserContextHolder;
import com.clp.service.ContentPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContentPublishServiceImpl implements ContentPublishService {

    private final PublisherContentRepository publisherContentRepository;
    private final ContentRepository contentRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public ContentPublishServiceImpl(PublisherContentRepository publisherContentRepository,
                                     ContentRepository contentRepository,
                                     PublisherRepository publisherRepository
    ) {
        this.publisherContentRepository = publisherContentRepository;
        this.contentRepository = contentRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public String publishContentRequest(Long contentId, Long publisherId) {

        User user = UserContextHolder.getUser();
        if (user != null && Role.WRITER.equals(user.getRole())) {
            Content content = contentRepository.findById(contentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
            Publisher publisher = publisherRepository.findById(publisherId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));
            PublisherContent publisherContent = PublisherContent.builder().content(content)
                    .publisher(publisher)
                    .status(Status.PENDING)
                    .build();

            publisherContentRepository.save(publisherContent);
            return "Request sent successfully";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}
