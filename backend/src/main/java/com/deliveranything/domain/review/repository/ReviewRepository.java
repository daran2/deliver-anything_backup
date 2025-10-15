package com.deliveranything.domain.review.repository;

import com.deliveranything.domain.review.entity.Review;
import com.deliveranything.domain.review.enums.ReviewTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

  Double findAvgRatingByTargetIdAndTargetType(Long targetId, ReviewTargetType targetType);

  Double findAvgRatingByCustomerProfileId(Long userId);
}
