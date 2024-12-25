package com.clp.mapper;

import com.clp.dto.WriterDto;
import com.clp.entity.Writer;
import org.springframework.stereotype.Component;

@Component
public class WriterMapper {

    public Writer dtoToEntity(WriterDto dto){
        return Writer.builder()
                .about(dto.getAbout())
                .name(dto.getName())
                .address(dto.getAddress())
                .build();
    }
}
