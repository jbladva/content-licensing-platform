package com.clp.service;

import com.clp.dto.ContentDto;
import com.clp.dto.DownloadContentDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ContentService {

    ContentDto uploadContent(ContentDto content, MultipartFile file);
    ContentDto getContent(Long id);
    List<ContentDto> getContentListByUser();
    List<ContentDto> getAllContentList();
    DownloadContentDto downloadContent(Long contentId);
    ContentDto updateContent(ContentDto content, MultipartFile file, Long contentId);
    String deleteContent(Long contentId);
}
