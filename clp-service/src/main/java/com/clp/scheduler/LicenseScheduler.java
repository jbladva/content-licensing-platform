package com.clp.scheduler;

import com.clp.entity.License;
import com.clp.entity.PublisherContent;
import com.clp.enums.Actions;
import com.clp.enums.Status;
import com.clp.repository.LicenseRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.service.WriterEmailService;
import com.clp.service.impl.PublisherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class LicenseScheduler {

    private final LicenseRepository licenseRepository;
    private final PublisherContentRepository publisherContentRepository;
    private final WriterEmailService writerEmailService;

    @Autowired
    LicenseScheduler(LicenseRepository licenseRepository,
                     PublisherContentRepository publisherContentRepository, WriterEmailService writerEmailService) {
        this.licenseRepository = licenseRepository;
        this.publisherContentRepository = publisherContentRepository;
        this.writerEmailService = writerEmailService;
    }

    /**
     * Run Scheduler every midnight at 12:00 AM
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void licenseSchedule() {
        List<License> licenses = licenseRepository.findAllByExpiryDateIsBefore(LocalDateTime.now());
        if (licenses.isEmpty()) {
            log.info("LicenseScheduler : license list is empty");
            return;
        }
        List<Long> contentIds = licenses.stream().map(license -> license.getContent().getId()).toList();

        List<PublisherContent> publisherContents = publisherContentRepository
                .findAllByContent_IdInAndStatus(contentIds, Status.PUBLISHED);

        if (publisherContents.isEmpty()) {
            log.info("LicenseScheduler : publisherContents list is empty");
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        publisherContents.forEach(publisherContent -> {

            publisherContent.setStatus(Status.UNPUBLISHED);

            publisherContentRepository.save(publisherContent);

            executorService.submit(() -> writerEmailService
                    .buildContentPublishOrUnpublishMessage(publisherContent, Actions.UNPUBLISHED));
        });
    }
}
