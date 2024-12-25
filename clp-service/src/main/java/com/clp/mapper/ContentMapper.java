package com.clp.mapper;

import com.clp.dto.ContentDto;
import com.clp.dto.WriterDto;
import com.clp.entity.Content;
import org.springframework.stereotype.Component;

@Component
public class ContentMapper {

    public ContentDto entityToDto(Content content) {
        return ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .contentLanguage(content.getContentLanguage())
                .description(content.getDescription())
                .createdDate(content.getCreatedDate())
                .updatedDate(content.getUpdatedDate())
                .writer(WriterDto.builder()
                        .name(content.getWriter().getName())
                        .about(content.getWriter().getAbout())
                        .address(content.getWriter().getAddress())
                        .build())
                .build();
    }
}
