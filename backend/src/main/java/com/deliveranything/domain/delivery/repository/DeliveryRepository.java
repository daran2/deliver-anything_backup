package com.deliveranything.domain.delivery.repository;

import com.deliveranything.domain.delivery.entity.Delivery;
import com.deliveranything.domain.delivery.enums.DeliveryStatus;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

  @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 'COMPLETED' " +
         "AND d.riderProfile.id = :riderProfileId " +
         "AND FUNCTION('DATE', d.completedAt) = CURRENT_DATE")
    // DATE() -> FUNCTION('DATE', ...) 수정
  Long countTodayCompletedDeliveriesByRider(Long riderProfileId);

  @Query("SELECT COUNT(d) FROM Delivery d WHERE d.riderProfile.id = :riderProfileId " +
         "AND d.status = 'COMPLETED' AND d.completedAt >= :weekStart")
  Long countThisWeekCompletedDeliveriesByRiderProfileId(
      @Param("riderProfileId") Long riderProfileId,
      @Param("weekStart") LocalDateTime weekStart
  );

  @Query("SELECT d FROM Delivery d WHERE d.riderProfile.id = :riderProfileId " +
         "AND d.status = 'COMPLETED' AND FUNCTION('DATE', d.completedAt) = CURRENT_DATE")
    // DATE() -> FUNCTION('DATE', ...) 수정
  List<Delivery> findTodayCompletedDeliveriesByRider(@Param("riderProfileId") Long riderProfileId);

  // IN_PROGRESS 같은 단일 배달 조회용 (Optional)
  Optional<Delivery> findFirstByRiderProfileIdAndStatusOrderByStartedAtDesc(
      Long riderProfileId, DeliveryStatus deliveryStatus
  );

  @Query("SELECT SUM(d.charge) FROM Delivery d WHERE d.riderProfile.id = :riderProfileId " +
         "AND d.status = 'COMPLETED'")
  Long sumTotalDeliveryChargesByRider(Long riderProfileId);

  List<Delivery> findByRiderProfileId(Long riderProfileId);

  // 라이더별 상태 조회 (List 반환) - JOIN FETCH로 N+1 방지
  @Query("SELECT d FROM Delivery d " +
         "JOIN FETCH d.store s " +
         "WHERE d.riderProfile.id = :riderProfileId AND d.status = :status")
  List<Delivery> findByRiderProfileIdAndStatus(
      @Param("riderProfileId") Long riderProfileId,
      @Param("status") DeliveryStatus status
  );
}
