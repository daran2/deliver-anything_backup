package com.deliveranything.domain.delivery.service;

import com.deliveranything.domain.delivery.dto.RiderLocationDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderLocationService {

  private final StringRedisTemplate redisTemplate;

  public static final String RIDER_GEO_KEY = "riders:location";

  public void saveRiderLocation(Long riderProfileId, RiderLocationDto location) {
    // Redis GEOADD 명령어를 사용하여 라이더 위치 저장
    redisTemplate.opsForGeo().add(RIDER_GEO_KEY,
        new Point(location.longitude(), location.latitude()),
        String.valueOf(riderProfileId));
  }
}
