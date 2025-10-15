package com.deliveranything.domain.auth.repository;

import com.deliveranything.domain.auth.entity.VerificationToken;
import com.deliveranything.domain.auth.enums.VerificationPurpose;
import com.deliveranything.domain.auth.enums.VerificationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  Optional<VerificationToken> findTopByIdentifierAndVerificationTypeAndPurposeOrderByCreatedAtDesc(
      String identifier,
      VerificationType verificationType,
      VerificationPurpose purpose
  );
}