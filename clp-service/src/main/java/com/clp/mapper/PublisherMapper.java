package com.clp.mapper;

import com.clp.dto.PublisherDto;
import com.clp.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher dtoToEntity(PublisherDto dto) {
        return Publisher.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .about(dto.getAbout())
                .organizationName(dto.getOrganizationName())
                .website(dto.getWebsite())
                .build();
    }
}
