package com.clp.dto;

import com.clp.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherContentDto {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String contentType;
    private String ContentLanguage;
    private Status contentStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private WriterDto writerDto;
}
