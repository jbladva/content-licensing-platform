package com.clp.controller;

import com.clp.dto.PublisherContentDto;
import com.clp.dto.PublisherDto;
import com.clp.dto.TrackDto;
import com.clp.enums.Status;
import com.clp.service.PublisherService;
import com.clp.service.TrackingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Tag(name = "Publisher API")
@CrossOrigin(maxAge = 3600)
public class PublisherController {

    private final PublisherService publisherService;
    private final TrackingService trackingService;

    @Autowired
    public PublisherController(PublisherService publisherService, TrackingService trackingService) {
        this.publisherService = publisherService;
        this.trackingService = trackingService;
    }

    @PostMapping("v1/publisher/create")
    public ResponseEntity<PublisherDto> createPublisherProfile(@RequestBody PublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.createPublisherProfile(publisherDto));
    }

    @GetMapping("v1/publisher/get")
    public ResponseEntity<PublisherDto> getPublisherProfile() {
        return ResponseEntity.ok(publisherService.getPublisherProfile());
    }

    @PostMapping("v1/publisher/content/approval")
    public ResponseEntity<String> acceptOrRejectContent(@RequestParam Long contentId,
                                                        @RequestParam String action) {
        return ResponseEntity.ok(publisherService.acceptOrRejectContent(contentId, action));
    }

    @GetMapping("v1/publisher/content/all")
    public ResponseEntity<List<PublisherContentDto>> getContentList(){
        return ResponseEntity.ok(publisherService.getAllContent());
    }

    @GetMapping("v1/publisher/content/get")
    public ResponseEntity<PublisherContentDto> getContentById(@RequestParam Long contentId){
        return ResponseEntity.ok(publisherService.getContent(contentId));
    }

    @PostMapping("v1/publisher/content/publish")
    public ResponseEntity<String> publishContent(@RequestParam Long contentId,
                                                 @RequestParam UUID licenseKey,
                                                 @RequestParam Status action){
        return ResponseEntity.ok(publisherService.publishContent(contentId, licenseKey, action));
    }

    @PostMapping("v1/publisher/content/track/amount")
    public ResponseEntity<String> calculateTrackAmount(@RequestParam Long contentId,
                                                          @RequestBody TrackDto trackDto){
        return ResponseEntity.ok(trackingService.calculateTrackAmount(contentId, trackDto));
    }
}
