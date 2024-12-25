package com.clp.service;

import com.clp.entity.PublisherContent;
import com.clp.enums.Actions;

public interface WriterEmailService {

    void buildContentAcceptOrRejectMessage(PublisherContent publisherContent, Actions actions);

    void buildContentPublishOrUnpublishMessage(PublisherContent publisherContent, Actions actions);
}
