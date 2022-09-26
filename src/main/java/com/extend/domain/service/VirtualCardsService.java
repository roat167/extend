package com.extend.domain.service;

import com.extend.domain.dto.VirtualCard;
import java.util.List;

public interface VirtualCardsService {
    List<VirtualCard> getVirtualCards(String accessToken);
}
