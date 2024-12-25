package com.clp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String description;
    private String fileUrl;
    private String contentType;
    private String contentLanguage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;
    @OneToOne(cascade = CascadeType.ALL)
    private License license;

}
