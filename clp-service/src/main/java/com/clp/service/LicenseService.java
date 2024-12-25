package com.clp.service;

import com.clp.dto.LicenseDto;
import org.springframework.stereotype.Component;

@Component
public interface LicenseService {

    LicenseDto generateLicense(Long contentId);
    LicenseDto getLicense(Long contentId);
}
