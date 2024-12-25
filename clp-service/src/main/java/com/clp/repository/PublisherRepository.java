package com.clp.repository;

import com.clp.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByUser_Id(long userId);
    Boolean existsByUser_Username(String username);
}
