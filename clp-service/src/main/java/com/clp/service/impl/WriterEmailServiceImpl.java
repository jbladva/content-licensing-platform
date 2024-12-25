package com.clp.service.impl;

import com.clp.dto.MailRequest;
import com.clp.entity.Content;
import com.clp.entity.Publisher;
import com.clp.entity.PublisherContent;
import com.clp.entity.Writer;
import com.clp.enums.Actions;
import com.clp.service.NotificationService;
import com.clp.service.WriterEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class WriterEmailServiceImpl implements WriterEmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final NotificationService notificationService;

    public WriterEmailServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void buildContentAcceptOrRejectMessage(PublisherContent publisherContent, Actions actions) {
        Writer writer = publisherContent.getContent().getWriter();
        Content content = publisherContent.getContent();
        Publisher publisher = publisherContent.getPublisher();
        Map<String, String> model = Map.of("receiver", writer.getName(),
                "contentTitle", content.getTitle(),
                "writerName", writer.getName(),
                "publisherName", publisher.getName(),
                "approvalDate", LocalDate.now().toString(),
                "action", actions.getAction());

        MailRequest contentReviewedNotification = MailRequest.builder()
                .name(writer.getName())
                .from(sender)
                .to(writer.getUser().getEmail())
                .subject("Content Reviewed Notification")
                .build();

        notificationService.sendActionNotification(contentReviewedNotification, model, "content-accept-reject.ftl");
    }

    @Override
    public void buildContentPublishOrUnpublishMessage(PublisherContent publisherContent, Actions actions) {
        Writer writer = publisherContent.getContent().getWriter();
        Map<String, String> model = Map.of("receiver", writer.getName(),
                "action", actions.getAction());

        MailRequest contentReviewedNotification = MailRequest.builder()
                .name(writer.getName())
                .from(sender)
                .to(writer.getUser().getEmail())
                .subject("Content Notification")
                .build();

        notificationService.sendActionNotification(contentReviewedNotification, model, "content-publish-unpublish.ftl");
    }
}
