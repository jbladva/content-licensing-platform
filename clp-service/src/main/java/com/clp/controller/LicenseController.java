package com.clp.controller;

import com.clp.dto.LicenseDto;
import com.clp.service.LicenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@Tag(name = "License API")
@CrossOrigin(maxAge = 3600)
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("v1/license/get")
    @PreAuthorize("@apiSecurity.hasPublisherRole()")
    public ResponseEntity<LicenseDto> generateLicense(@RequestParam Long contentId){
        return ResponseEntity.ok(licenseService.generateLicense(contentId));
    }
}
