package com.clp.entity;

import com.clp.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Content content;
    @ManyToOne(fetch = FetchType.EAGER)
    private Publisher publisher;
    private Status status;
    private LocalDate publishDate;
    private String reason;
    private Long visitCount;
    private Long downloadCont;
    private Double amount;
}
