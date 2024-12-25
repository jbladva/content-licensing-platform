package com.clp.repository;

import com.clp.entity.PublisherContent;
import com.clp.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherContentRepository extends JpaRepository<PublisherContent, Long> {

    List<PublisherContent> findAllByContent_Writer_User_Id(Long id);

    List<PublisherContent> findAllByContent_IdAndContent_Writer_User_Id(Long contentId, Long userId);

    List<PublisherContent> findAllByPublisher_User_Id(Long id);

    Optional<PublisherContent> findByContent_IdAndPublisher_User_Id(Long contentId, Long publisherUserId);

    List<PublisherContent> findAllByContent_IdInAndStatus(List<Long> contentIds, Status status);

    List<PublisherContent> findAllByContent_Id(Long contentId);
}
