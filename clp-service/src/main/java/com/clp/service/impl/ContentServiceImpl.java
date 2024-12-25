package com.clp.service.impl;

import com.amazonaws.util.IOUtils;
import com.clp.dto.ContentDto;
import com.clp.dto.DownloadContentDto;
import com.clp.entity.Content;
import com.clp.entity.PublisherContent;
import com.clp.entity.User;
import com.clp.enums.Status;
import com.clp.mapper.ContentMapper;
import com.clp.repository.ContentRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.repository.WriterRepository;
import com.clp.security.UserContextHolder;
import com.clp.service.ContentService;
import com.clp.util.S3BucketHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ContentServiceImpl implements ContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);
    @Value("${cloud.aws.bucket.clp.folderName}")
    private String folderName;

    private final ContentRepository contentRepository;
    private final WriterRepository writerRepository;
    private final PublisherContentRepository publisherContentRepository;
    private final ContentMapper contentMapper;
    private final ModelMapper modelMapper;
    private final S3BucketHelper s3BucketHelper;

    @Autowired
    ContentServiceImpl(ContentRepository contentRepository,
                       WriterRepository writerRepository,
                       PublisherContentRepository publisherContentRepository,
                       ContentMapper contentMapper,
                       ModelMapper modelMapper,
                       S3BucketHelper s3BucketHelper) {
        this.contentRepository = contentRepository;
        this.writerRepository = writerRepository;
        this.publisherContentRepository = publisherContentRepository;
        this.contentMapper = contentMapper;
        this.modelMapper = modelMapper;
        this.s3BucketHelper = s3BucketHelper;
    }

    @Override
    public ContentDto uploadContent(ContentDto contentDto, MultipartFile file) {
        return writerRepository.findByUser_Username(UserContextHolder.getUser().getUsername())
                .map(writer -> {
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    Content content = modelMapper.map(contentDto, Content.class);
                    content.setWriter(writer);
                    content.setCreatedDate(LocalDateTime.now());
                    content.setUpdatedDate(LocalDateTime.now());
                    content.setFileUrl(s3BucketHelper.uploadToS3(file, folderName));
                    content.setContentType(file.getContentType());
                    content.setCreatedDate(LocalDateTime.now());
                    content.setUpdatedDate(LocalDateTime.now());

                    return modelMapper.map(contentRepository.save(content), ContentDto.class);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Writer Not Found")
                );
    }

    @Override
    public ContentDto getContent(Long id) {

        Content content = contentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Content with id %s not found", id)));
        return contentMapper.entityToDto(content);
    }

    @Override
    public List<ContentDto> getContentListByUser() {
        User currentUser = UserContextHolder.getUser();

        List<Content> contents = contentRepository.findAllByWriter_User_Id(currentUser.getId());

        if (contents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content list is empty");
        }

        return contents.stream().map(contentMapper::entityToDto).toList();
    }

    @Override
    public List<ContentDto> getAllContentList() {
        return contentRepository.findAll().stream().map(contentMapper::entityToDto).toList();
    }

    @Override
    public DownloadContentDto downloadContent(Long contentId) {
        return contentRepository.findById(contentId)
                .map(content ->
                {
                    try {
                        return DownloadContentDto.builder()
                                .bytes(IOUtils.toByteArray(s3BucketHelper.getContentFile(content.getFileUrl()
                                        , content.getContentType())))
                                .content(content)
                                .build();

                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found");
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content Not Found"));
    }

    @Override
    public ContentDto updateContent(ContentDto contentDto, MultipartFile file, Long contentId) {
        User user = UserContextHolder.getUser();
        Content content = contentRepository.findByIdAndWriter_User_Id(contentId, user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Content with id %s not found", contentId))
        );
        if (contentDto == null && file == null) {
            return contentMapper.entityToDto(content);
        }
        if (contentDto != null) {
            content.setContentLanguage(contentDto.getContentLanguage());
            content.setTitle(contentDto.getTitle());
            content.setDescription(contentDto.getDescription());
        }
        if (file != null) {
            content.setFileUrl(s3BucketHelper.uploadToS3(file, folderName));
        }
        return contentMapper.entityToDto(contentRepository.save(content));
    }

    @Override
    public String deleteContent(Long contentId) {
        User user = UserContextHolder.getUser();
        Content content = contentRepository.findByIdAndWriter_User_Id(contentId, user.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Content with id %s not found", contentId))
        );
        List<PublisherContent> publisherContents = publisherContentRepository.findAllByContent_Id(contentId);
        if (publisherContents.isEmpty()) {
            contentRepository.delete(content);
        }
        List<PublisherContent> list = publisherContents.stream().filter(publisherContent ->
                publisherContent.getStatus().equals(Status.PUBLISHED)).toList();

        if (!list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Content already Published");
        }
        try {
            publisherContentRepository.deleteAll(publisherContents);
            contentRepository.delete(content);
        } catch (Exception e) {
            log.error("Error while deleting content", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting content");
        }
        return "Content with id " + contentId + " deleted";
    }
}
