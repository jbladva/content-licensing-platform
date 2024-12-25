package com.clp.controller;

import com.clp.dto.PublisherDto;
import com.clp.dto.WriterDto;
import com.clp.dto.WriterContentDto;
import com.clp.entity.Writer;
import com.clp.service.ContentPublishService;
import com.clp.service.PublisherService;
import com.clp.service.WriterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Writer API")
@CrossOrigin(maxAge = 3600)
public class WriterController {

    private final WriterService writerService;
    private final ContentPublishService contentPublishService;
    private final PublisherService publisherService;

    @Autowired
    public WriterController(WriterService writerService,
                            ContentPublishService contentPublishService,
                            PublisherService publisherService) {
        this.writerService = writerService;
        this.contentPublishService = contentPublishService;
        this.publisherService = publisherService;
    }

    @PostMapping("v1/writer/create")
    public ResponseEntity<WriterDto> save(@RequestBody WriterDto writerDto) {
        return ResponseEntity.ok(writerService.registerWriter(writerDto));
    }

    @GetMapping("v1/writer/get")
    public ResponseEntity<WriterDto> get(){
        return ResponseEntity.ok(writerService.getWriter());
    }

    @PostMapping("v1/writer/content/publish/request")
    public ResponseEntity<String> requestPublish(@RequestParam Long contentId, @RequestParam Long publisherId) {
        return ResponseEntity.ok(contentPublishService.publishContentRequest(contentId, publisherId));
    }

    @GetMapping("v1/writer/content/all")
    public ResponseEntity<List<WriterContentDto>> getContentList(){
        return ResponseEntity.ok(writerService.getWriterContents());
    }

    @GetMapping("v1/writer/content/get")
    public ResponseEntity<WriterContentDto> getContentById(@RequestParam Long contentId){
        return ResponseEntity.ok(writerService.getContentById(contentId));
    }

    @GetMapping("v1/writer/publisher/all")
    @PreAuthorize("@apiSecurity.hasWriterRole()")
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getPublishers());
    }
}
