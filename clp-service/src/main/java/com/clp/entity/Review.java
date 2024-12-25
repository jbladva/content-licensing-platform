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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Content content;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
    private String comment;
    private LocalDateTime createTime;
}
