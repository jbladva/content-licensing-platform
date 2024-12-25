package com.clp.service;

import com.clp.dto.TrackDto;
import org.springframework.stereotype.Component;

@Component
public interface TrackingService {

    String calculateTrackAmount(Long contentId, TrackDto dto);
}
