package com.clp.service.impl;

import com.clp.dto.LicenseDto;
import com.clp.entity.Content;
import com.clp.entity.License;
import com.clp.entity.Publisher;
import com.clp.entity.PublisherContent;
import com.clp.enums.Actions;
import com.clp.enums.Status;
import com.clp.repository.ContentRepository;
import com.clp.repository.LicenseRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.repository.PublisherRepository;
import com.clp.security.RequestContext;
import com.clp.security.UserContextHolder;
import com.clp.service.LicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;
    private final PublisherContentRepository publisherContentRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public LicenseServiceImpl(LicenseRepository licenseRepository,
                              ModelMapper modelMapper,
                              ContentRepository contentRepository, PublisherContentRepository publisherContentRepository, PublisherRepository publisherRepository) {
        this.licenseRepository = licenseRepository;
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
        this.publisherContentRepository = publisherContentRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public LicenseDto generateLicense(Long contentId) {
        PublisherContent publisherContentEntity = publisherContentRepository.findByContent_IdAndPublisher_User_Id(contentId, UserContextHolder.getUser().getId())
                .map(publisherContent -> {
                    if (!publisherContent.getStatus().equals(Status.APPROVED))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is not approved");
                    return publisherContent;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content Not Found!!"));

        Content content = contentRepository.findById(contentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Content with id %s not found", contentId)));
        return licenseRepository.findByContent_Id(contentId).map(license ->
                modelMapper.map(license, LicenseDto.class)
        ).orElseGet(() -> {
            License license = License.builder()
                    .licenseKey(UUID.randomUUID())
                    .content(content)
                    .issueDate(LocalDateTime.now())
                    .licenseeUserID(publisherContentEntity.getPublisher().getId())
                    .licensorUserID(publisherContentEntity.getContent().getWriter().getId())
                    .expiryDate(LocalDateTime.now().plusYears(1))
                    .build();
            content.setLicense(license);
//        contentRepository.save(content);
            return modelMapper.map(licenseRepository.save(license), LicenseDto.class);
        });

    }

    @Override
    public LicenseDto getLicense(Long contentId) {
        return modelMapper.map(licenseRepository.findByContent_Id(contentId), LicenseDto.class);
    }
}
