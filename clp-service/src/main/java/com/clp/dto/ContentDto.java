package com.clp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {

    private Long id;
    private String title;
    private String description;
    private String contentType;
    private String contentLanguage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private WriterDto writer;
}
