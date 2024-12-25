package com.clp.dto;


import com.clp.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterContentDto {

    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String contentType;
    private String ContentLanguage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ContentPublisherDto> publishers;
}
