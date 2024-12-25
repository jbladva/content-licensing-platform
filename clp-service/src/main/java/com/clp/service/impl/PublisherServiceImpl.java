package com.clp.service.impl;

import com.clp.dto.PublisherContentDto;
import com.clp.dto.PublisherDto;
import com.clp.dto.WriterDto;
import com.clp.entity.*;
import com.clp.enums.Actions;
import com.clp.enums.Status;
import com.clp.mapper.PublisherMapper;
import com.clp.repository.LicenseRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.repository.PublisherRepository;
import com.clp.security.UserContextHolder;
import com.clp.service.PublisherService;
import com.clp.service.WriterEmailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    private final PublisherContentRepository publisherContentRepository;
    private final LicenseRepository licenseRepository;
    private final WriterEmailService writerEmailService;
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository,
                                PublisherMapper publisherMapper,
                                PublisherContentRepository publisherContentRepository,
                                LicenseRepository licenseRepository,
                                WriterEmailService writerEmailService,
                                ModelMapper modelMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
        this.publisherContentRepository = publisherContentRepository;
        this.licenseRepository = licenseRepository;
        this.writerEmailService = writerEmailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PublisherDto createPublisherProfile(PublisherDto dto) {
        Publisher publisher = publisherMapper.dtoToEntity(dto);
        User currentUser = UserContextHolder.getUser();

        if (ObjectUtils.isEmpty(currentUser))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to perform this operation");

        publisher.setUser(currentUser);

        return modelMapper.map(publisherRepository.save(publisher),PublisherDto.class);
    }

    @Override
    public PublisherDto getPublisherProfile() {
        User user = UserContextHolder.getUser();
        Publisher publisher = publisherRepository.findByUser_Id(user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found")
        );
        return modelMapper.map(publisher,PublisherDto.class);
    }

    @Override
    public List<PublisherDto> getPublishers() {
        List<PublisherDto> publisherDtos = modelMapper.map(publisherRepository.findAll(),
                new TypeToken<List<PublisherDto>>() {
                }.getType());

        return publisherDtos.stream().map(publisherDto -> {
            publisherDto.setUser(null);
            return publisherDto;
        }).toList();
    }

    @Override
    public String acceptOrRejectContent(Long contentId, String action) {
        publisherContentRepository.findByContent_IdAndPublisher_User_Id(contentId, UserContextHolder.getUser().getId())
                .ifPresentOrElse(publisherContent -> {
                            ExecutorService executorService = Executors.newFixedThreadPool(10);
                            switch (action) {
                                case "accept" -> publisherContent.setStatus(Status.APPROVED);
                                case "reject" -> {
                                    publisherContent.setStatus(Status.REJECTED);
                                    executorService.submit(new ActionNotification(Actions.REJECT, publisherContent));
                                }
                                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Please select the correct action either accept or reject.");
                            }
                            publisherContentRepository.save(publisherContent);
                        },
                        () -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Publisher not found with given content " + contentId);
                        });
        return "Action taken successfully";
    }

    @Override
    public List<PublisherContentDto> getAllContent() {
        User user = UserContextHolder.getUser();

        List<PublisherContent> publisherContents = publisherContentRepository.findAllByPublisher_User_Id(user.getId());
        return publisherContents.stream()
                .map(this::mapToPublisherContentDto)
                .toList();
    }

    @Override
    public PublisherContentDto getContent(Long contentId) {
        User user = UserContextHolder.getUser();

        PublisherContent publisherContent = publisherContentRepository
                .findByContent_IdAndPublisher_User_Id(contentId, user.getId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST,"Content not found"));

        return publisherContent == null ? null : mapToPublisherContentDto(publisherContent);
    }

    @Override
    public String publishContent(Long contentId, UUID licenseKey, Status status) {
        User user = UserContextHolder.getUser();
        License license = licenseRepository.findByContent_IdAndLicenseKey(contentId, licenseKey).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")
        );

        if (license.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License expired");
        }

        PublisherContent publisherContent = publisherContentRepository
                .findByContent_IdAndPublisher_User_Id(contentId, user.getId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Content with id %s not found", contentId))
                );

        if (status.equals(publisherContent.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Content status is already " + status);
        }
        if (publisherContent.getStatus().equals(Status.APPROVED) && status.equals(Status.UNPUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Content is not published yet");
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if ((publisherContent.getStatus().equals(Status.APPROVED) ||
                publisherContent.getStatus().equals(Status.UNPUBLISHED)) && status.equals(Status.PUBLISHED)) {
            publisherContent.setStatus(Status.PUBLISHED);
            publisherContentRepository.save(publisherContent);
            executorService.submit(new ActionNotification(Actions.PUBLISHED,publisherContent));
            return "Content published successfully";
        }
        if (publisherContent.getStatus().equals(Status.PUBLISHED) && status.equals(Status.UNPUBLISHED)) {
            publisherContent.setStatus(Status.UNPUBLISHED);
            publisherContentRepository.save(publisherContent);
            executorService.submit(new ActionNotification(Actions.UNPUBLISHED,publisherContent));
            return "Content published successfully";
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Content with id %s is not approved", contentId));
    }

    private PublisherContentDto mapToPublisherContentDto(PublisherContent publisherContent) {
        Content content = publisherContent.getContent();

        return PublisherContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .fileUrl(content.getFileUrl())
                .contentType(content.getContentType())
                .ContentLanguage(content.getContentLanguage())
                .contentStatus(publisherContent.getStatus())
                .createdDate(content.getCreatedDate())
                .updatedDate(content.getUpdatedDate())
                .writerDto(WriterDto.builder()
                        .name(content.getWriter().getName())
                        .address(content.getWriter().getAddress())
                        .about(content.getWriter().getAbout())
                        .build())
                .build();
    }

    class ActionNotification implements Runnable {

        private final Actions actions;
        private final PublisherContent publisherContent;

        ActionNotification(Actions actions, PublisherContent publisherContent) {
            this.actions = actions;
            this.publisherContent = publisherContent;
        }

        @Override
        public void run() {

            switch (actions) {
                case ACCEPT -> writerEmailService.buildContentAcceptOrRejectMessage(publisherContent, Actions.ACCEPT);

                case REJECT -> writerEmailService.buildContentAcceptOrRejectMessage(publisherContent, Actions.REJECT);

                case PUBLISHED -> writerEmailService.buildContentPublishOrUnpublishMessage(publisherContent,Actions.PUBLISHED);

                case UNPUBLISHED -> writerEmailService.buildContentPublishOrUnpublishMessage(publisherContent,Actions.UNPUBLISHED);

                default -> log.error("Unable to send notification");
            }
        }
    }
}
