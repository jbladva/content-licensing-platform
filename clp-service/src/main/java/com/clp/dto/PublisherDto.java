package com.clp.dto;

import com.clp.Views;
import com.clp.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublisherDto {

    private Long id;

    private String name;

    private String address;

    private String about;

    private String organizationName;

    private String website;

    private UserDto user;
}
