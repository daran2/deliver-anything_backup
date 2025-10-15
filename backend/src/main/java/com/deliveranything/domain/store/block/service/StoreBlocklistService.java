package com.deliveranything.domain.store.block.service;

import com.deliveranything.domain.store.block.repository.StoreBlocklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StoreBlocklistService {

  private final StoreBlocklistRepository storeBlocklistRepository;

}
