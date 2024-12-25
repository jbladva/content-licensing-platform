package com.clp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackDto {
    Long visitCount;
    Long downloadCount;
    Double amount;
}
