package com.deliveranything.domain.notification.scheduler;

import com.deliveranything.domain.user.profile.entity.Profile;
import com.deliveranything.domain.user.profile.repository.ProfileRepository;
import com.deliveranything.domain.user.user.entity.User;
import com.deliveranything.domain.user.user.repository.UserRepository;
import com.deliveranything.global.infra.SmsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private final SmsService smsService;

  // 1시간마다 실행
  @Scheduled(cron = "0 0 * * * *")
  public void sendReviewNotificationsHourly() {
    log.info("===== 1시간 알림 스케줄 시작 =====");

    // 1. SMS 발송 대상 사용자 조회 (판매자 + 라이더 등)
    List<User> allUsers = userRepository.findAll();

    for (User user : allUsers) {
      // 유저가 가진 모든 프로필 조회
      List<Profile> profiles = profileRepository.findAllByUser(user);

      Map<String, Integer> totalCounts = new HashMap<>();

      for (Profile profile : profiles) {
        String redisKey = "notifications:hourly:profile:" + profile.getId();

        // 2. Redis 에서 해당 프로필의 알림 카운트 조회
        Map<Object, Object> counts = redisTemplate.opsForHash().entries(redisKey);

        counts.forEach((type, count) -> {
          int value = count != null ? Integer.parseInt(count.toString()) : 0;
          String key = type.toString() + ":" + profile.getType().name();
          totalCounts.merge(key, value, Integer::sum);
        });

        // 3. 한 프로필 처리 후 Redis 초기화
        if (!counts.isEmpty()) {
          redisTemplate.delete(redisKey);
        }
      }

      // 4. 합산된 결과로 SMS 발송
      if (!totalCounts.isEmpty()) {
        StringBuilder message = new StringBuilder("지난 1시간 동안 새 알림: ");

        totalCounts.forEach((key, count) -> {
          String[] parts = key.split(":");
          String type = parts[0];
          String profileType = parts[1];
          String displayType = switch (type) {
            case "NEW_REVIEW" -> displayType = "리뷰";
            // 1시간 집계 알림 확장 시 추가 요망
            default -> displayType = type;
          };

          message.append(displayType)
              .append("(")
              .append(profileType)
              .append(") ")
              .append(count)
              .append("건, ");
        });

        // 마지막 쉼표 제거
        if (message.length() > 2) {
          message.setLength(message.length() - 2);
        }

        try {
          smsService.sendSms(user.getPhoneNumber(), message.toString());
          log.info("SMS 전송 완료: {}, 내용: {}", user.getPhoneNumber(), message);
        } catch (Exception e) {
          log.error("SMS 전송 실패: {}", user.getPhoneNumber(), e);
        }
      }
    }

    log.info("===== 1시간 단위 알림 스케줄러 종료 =====");
  }
}