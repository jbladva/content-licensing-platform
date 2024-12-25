package com.clp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Content content;
    private UUID licenseKey;
    private Long licensorUserID;
    private Long licenseeUserID;
    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
}
