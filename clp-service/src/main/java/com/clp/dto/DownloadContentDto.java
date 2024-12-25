package com.clp.dto;

import com.clp.entity.Content;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadContentDto {
    private Content content;
    private byte[] bytes;

}
