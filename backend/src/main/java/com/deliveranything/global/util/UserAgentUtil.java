package com.deliveranything.global.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAgentUtil {

  /**
   * HTTP 요청에서 기기 정보 추출
   */
  public String extractDeviceInfo(HttpServletRequest request) {
    String userAgentString = request.getHeader("User-Agent");

    if (userAgentString == null || userAgentString.isBlank()) {
      log.warn("User-Agent 헤더가 없습니다.");
      return "Unknown Device";
    }

    try {
      UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

      DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
      OperatingSystem os = userAgent.getOperatingSystem();
      Browser browser = userAgent.getBrowser();

      // 포맷: "기기타입 - OS (브라우저)"
      String deviceInfo = String.format("%s - %s (%s)",
          getDeviceTypeName(deviceType),
          os.getName(),
          browser.getName()
      );

      log.debug("추출된 기기 정보: {}", deviceInfo);
      return deviceInfo;

    } catch (Exception e) {
      log.warn("User-Agent 파싱 실패: {}", userAgentString, e);
      return "Unknown Device";
    }
  }

  /**
   * DeviceType을 한글로 변환 (선택사항)
   */
  private String getDeviceTypeName(DeviceType deviceType) {
    return switch (deviceType) {
      case COMPUTER -> "PC";
      case MOBILE -> "Mobile";
      case TABLET -> "Tablet";
      case GAME_CONSOLE -> "Game Console";
      case DMR -> "Media Player";
      case WEARABLE -> "Wearable";
      default -> "Unknown";
    };
  }

  /**
   * 간단한 버전 (기기타입만)
   */
  public String extractSimpleDeviceInfo(HttpServletRequest request) {
    String userAgentString = request.getHeader("User-Agent");

    if (userAgentString == null || userAgentString.isBlank()) {
      return "Unknown";
    }

    try {
      UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
      DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
      OperatingSystem os = userAgent.getOperatingSystem();

      // 간단한 포맷: "기기타입 - OS"
      return String.format("%s - %s",
          getDeviceTypeName(deviceType),
          os.getName()
      );

    } catch (Exception e) {
      log.warn("User-Agent 파싱 실패", e);
      return "Unknown";
    }
  }
}