package com.deliveranything.domain.search.store.service;

import com.deliveranything.domain.search.store.document.StoreDocument;
import com.deliveranything.domain.search.store.dto.StoreSearchRequest;
import com.deliveranything.domain.search.store.dto.StoreSearchResponse;
import com.deliveranything.domain.search.store.repository.StoreSearchRepository;
import com.deliveranything.global.common.CursorPageResponse;
import com.deliveranything.global.util.GeoUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreSearchService {

  private final StoreSearchRepository storeSearchRepository;

  public CursorPageResponse<StoreSearchResponse> search(StoreSearchRequest request) {
    CursorPageResponse<StoreDocument> results = storeSearchRepository.search(request);

    List<StoreSearchResponse> dtoList = results.content().stream()
        .map(doc -> {
          double distanceKm = GeoUtil.distanceKm(
              request.lat(), request.lng(),
              doc.getLocation().getLat(), doc.getLocation().getLon()
          );
          int deliveryFee = estimateDeliveryFee(distanceKm);
          return new StoreSearchResponse(
              doc.getId(),
              doc.getName(),
              doc.getRoadAddress(),
              doc.getStatus(),
              doc.getImageUrl(),
              doc.getCategoryName(),
              distanceKm,
              deliveryFee
          );
        })
        .toList();

    return new CursorPageResponse<>(dtoList, results.nextPageToken(), results.hasNext());
  }

  private int estimateDeliveryFee(double distanceKm) {
    final int BASE_DISTANCE_KM = 3;      // 기본 거리
    final int BASE_FEE = 3000;           // 기본 요금 (3km까지)
    final int EXTRA_FEE_PER_KM = 1000;   // 추가 km당 요금

    if (distanceKm <= BASE_DISTANCE_KM) {
      return BASE_FEE;
    } else {
      // 초과 km 계산 (소수점 올림)
      int extraKm = (int) Math.ceil(distanceKm - BASE_DISTANCE_KM);
      return BASE_FEE + extraKm * EXTRA_FEE_PER_KM;
    }
  }
}
