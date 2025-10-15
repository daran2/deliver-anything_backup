package com.deliveranything.domain.user.profile.repository;

import com.deliveranything.domain.user.profile.entity.RiderProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderProfileRepository extends JpaRepository<RiderProfile, Long> {

  Optional<RiderProfile> findById(Long profileId);

}
