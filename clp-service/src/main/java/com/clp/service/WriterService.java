package com.clp.service;

import com.clp.dto.WriterDto;
import com.clp.dto.WriterContentDto;
import com.clp.entity.Writer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WriterService {

    WriterDto registerWriter(WriterDto writer);
    WriterDto getWriter();
    List<WriterContentDto> getWriterContents();
    WriterContentDto getContentById(Long contentId);
}
