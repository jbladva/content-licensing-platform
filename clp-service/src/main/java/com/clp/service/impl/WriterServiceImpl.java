package com.clp.service.impl;

import com.clp.dto.ContentPublisherDto;
import com.clp.dto.WriterContentDto;
import com.clp.dto.WriterDto;
import com.clp.entity.Content;
import com.clp.entity.PublisherContent;
import com.clp.entity.User;
import com.clp.entity.Writer;
import com.clp.mapper.WriterMapper;
import com.clp.repository.ContentRepository;
import com.clp.repository.PublisherContentRepository;
import com.clp.repository.WriterRepository;
import com.clp.security.UserContextHolder;
import com.clp.service.WriterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WriterServiceImpl implements WriterService {

    private final WriterMapper writerMapper;
    private final WriterRepository writerRepository;
    private final PublisherContentRepository publisherContentRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WriterServiceImpl(WriterMapper writerMapper,
                             WriterRepository writerRepository,
                             PublisherContentRepository publisherContentRepository,
                             ContentRepository contentRepository, ModelMapper modelMapper) {
        this.writerMapper = writerMapper;
        this.writerRepository = writerRepository;
        this.publisherContentRepository = publisherContentRepository;
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public WriterDto registerWriter(WriterDto dto) {
        Writer writer = writerMapper.dtoToEntity(dto);
        User currentUser = UserContextHolder.getUser();
        if (currentUser != null) {
            writer.setUser(currentUser);
        }
        return modelMapper.map(writerRepository.save(writer), WriterDto.class);
    }

    @Override
    public WriterDto getWriter() {
        User user = UserContextHolder.getUser();
        Writer writer = writerRepository.findByUser_Username(user.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Writer Not Found")
        );
        return modelMapper.map(writer, WriterDto.class);
    }


    @Override
    public List<WriterContentDto> getWriterContents() {

        User user = UserContextHolder.getUser();
        List<Content> contents = contentRepository.findAllByWriter_User_Id(user.getId());
        List<PublisherContent> publisherContents = publisherContentRepository
                .findAllByContent_Writer_User_Id(user.getId());
        if (publisherContents.isEmpty()) {
            return contents.stream().map(content ->
                    mapToWriterContentDto(content, Collections.emptyList())).collect(Collectors.toList());
        }
        List<WriterContentDto> writerContentDtos = new ArrayList<>();
        Map<Long, List<PublisherContent>> listMap = publisherContents.stream()
                .collect(Collectors.groupingBy(publisherContent ->
                        publisherContent.getContent().getId(), Collectors.toList()));

        contents.forEach(content -> {
            if (listMap.containsKey(content.getId())) {
                writerContentDtos.add(mapToWriterContentDto(content, listMap.get(content.getId())));
                return;
            }
            writerContentDtos.add(mapToWriterContentDto(content, Collections.emptyList()));
        });

        return writerContentDtos;
    }

    @Override
    public WriterContentDto getContentById(Long contentId) {
        User user = UserContextHolder.getUser();

        List<PublisherContent> publisherContents = publisherContentRepository
                .findAllByContent_IdAndContent_Writer_User_Id(contentId, user.getId());
        if (publisherContents.isEmpty()) {
            return contentRepository.findById(contentId).map(content ->
                    mapToWriterContentDto(content, Collections.emptyList()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Content not found"));
        }

        return publisherContents.stream()
                .collect(Collectors.groupingBy(PublisherContent::getContent, Collectors.toList()))
                .entrySet().stream()
                .findFirst()
                .map(entry -> mapToWriterContentDto(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    private WriterContentDto mapToWriterContentDto(Content content, List<PublisherContent> publisherContents) {
        List<ContentPublisherDto> publishers = publisherContents.stream()
                .map(pc -> ContentPublisherDto.builder()
                        .id(pc.getId())
                        .name(pc.getPublisher().getName())
                        .address(pc.getPublisher().getAddress())
                        .about(pc.getPublisher().getAbout())
                        .organizationName(pc.getPublisher().getOrganizationName())
                        .website(pc.getPublisher().getWebsite())
                        .contentStatus(pc.getStatus())
                        .build())
                .collect(Collectors.toList());

        return WriterContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .fileUrl(content.getFileUrl())
                .contentType(content.getContentType())
                .ContentLanguage(content.getContentLanguage())
                .createdDate(content.getCreatedDate())
                .updatedDate(content.getUpdatedDate())
                .publishers(publishers)
                .build();
    }

}
