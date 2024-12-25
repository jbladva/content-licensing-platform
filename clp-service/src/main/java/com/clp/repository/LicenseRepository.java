package com.clp.repository;

import com.clp.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    Optional<License> findByContent_Id(Long contentId);
    Optional<License> findByContent_IdAndLicenseKey(Long contentId, UUID licenseKey);
    List<License> findAllByExpiryDateIsBefore(LocalDateTime currentDate);
}
