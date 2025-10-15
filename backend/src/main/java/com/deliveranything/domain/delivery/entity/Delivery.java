package com.deliveranything.domain.delivery.entity;


import com.deliveranything.domain.delivery.enums.DeliveryStatus;
import com.deliveranything.domain.review.entity.Review;
import com.deliveranything.domain.store.store.entity.Store;
import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import com.deliveranything.domain.user.profile.entity.RiderProfile;
import com.deliveranything.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "deliveries")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

//  @Column(name = "delivery_remaining_time", nullable = false)
//  private Integer remainingTime;

  @Column(name = "delivery_expected_time", nullable = false)
  private Double expectedTime;

  @Column(name = "delivery_requested")
  private String requested;

  @Column(name = "delivery_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

  @Column(name = "delivery_started_at")
  private LocalDateTime startedAt;

  @Column(name = "delivery_completed_at")
  private LocalDateTime completedAt;

  @Column(name = "delivery_charge")
  private Integer charge;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "review_id")
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rider_profile_id")
  private RiderProfile riderProfile;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_profile_id")
  private CustomerProfile customer;


  @Builder
  public Delivery(Double expectedTime, String requested, DeliveryStatus status,
      LocalDateTime startedAt, LocalDateTime completedAt, Integer charge,
      Store store, Review review, RiderProfile riderProfile, CustomerProfile customer) {
    this.expectedTime = expectedTime;
    this.requested = requested;
    this.status = status;
    this.startedAt = startedAt;
    this.completedAt = completedAt;
    this.charge = charge;
    this.store = store;
    this.review = review;
    this.riderProfile = riderProfile;
    this.customer = customer;
  }

  public void updateStatus(DeliveryStatus newStatus) {
    this.status = newStatus;
  }

  public void updateStartedAt(LocalDateTime now) {
    this.startedAt = now;
  }

  public void updateCompletedAt(LocalDateTime now) {
    this.completedAt = now;
  }
}
