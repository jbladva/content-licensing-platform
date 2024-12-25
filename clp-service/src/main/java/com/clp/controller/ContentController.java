package com.clp.controller;

import com.clp.dto.ContentDto;
import com.clp.dto.DownloadContentDto;
import com.clp.security.ApiSecurity;
import com.clp.service.ContentService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Content API")
@CrossOrigin(maxAge = 3600)
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping(value = "v1/content/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @RequestBody(content = @Content(encoding = @Encoding(name = "content", contentType = "application/json")))
    @PreAuthorize("@apiSecurity.hasWriterRole()")
    public ResponseEntity<ContentDto> uploadContent(@RequestPart ContentDto content, @RequestPart MultipartFile file) {
        return ResponseEntity.ok(contentService.uploadContent(content, file));
    }

    @GetMapping("v1/content/download/{contentId}")
    @PreAuthorize("@apiSecurity.hasWriterRole() or @apiSecurity.hasPublisherRole()")
    public ResponseEntity<ByteArrayResource> downloadContent(@PathVariable Long contentId){
        DownloadContentDto downloadContentDto = contentService.downloadContent(contentId);
        ByteArrayResource resource = new ByteArrayResource(downloadContentDto.getBytes());
        return ResponseEntity
                .ok()
                .contentLength(resource.getByteArray().length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=\"" + downloadContentDto.getContent().getFileUrl() +"\"")
                .body(resource);
    }

    @PutMapping(value = "v1/content/update",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @RequestBody(content = @Content(encoding = @Encoding(name = "content", contentType = "application/json")))
    @PreAuthorize("@apiSecurity.hasWriterRole()")
    public ResponseEntity<ContentDto> updateContent(@RequestParam Long contentId,
                                                    @RequestPart(required = false) ContentDto content,
                                                    @RequestPart(required = false) MultipartFile file){
        return ResponseEntity.ok(contentService.updateContent(content, file, contentId));
    }

    @DeleteMapping("v1/content/delete")
    public ResponseEntity<String> deleteContent(@RequestParam Long contentId){
        return ResponseEntity.ok(contentService.deleteContent(contentId));
    }

    @GetMapping("v1/public/content/all")
    public ResponseEntity<List<ContentDto>> getAllContents(){
        return ResponseEntity.ok(contentService.getAllContentList());
    }

    @GetMapping("v1/public/content")
    public ResponseEntity<ContentDto> getContentById(@RequestParam Long contentId){
        return ResponseEntity.ok(contentService.getContent(contentId));
    }
}
