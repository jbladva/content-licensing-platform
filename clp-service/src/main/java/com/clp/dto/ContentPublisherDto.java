package com.clp.dto;

import com.clp.Views;
import com.clp.enums.Status;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonView(Views.PublicView.class)
public class ContentPublisherDto {

    private Long id;
    private String name;
    private String address;
    private String about;
    private String organizationName;
    private String website;
    private Status contentStatus;
}
