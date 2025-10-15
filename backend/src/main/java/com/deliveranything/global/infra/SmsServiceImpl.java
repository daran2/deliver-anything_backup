package com.deliveranything.global.infra;

import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

  @Override
  public void sendSms(String phoneNumber, String message) {
    System.out.println("[SMS 테스트] 받는 사람: " + phoneNumber + ", 메시지: " + message);
    //TODO: 배포시 실제 sms api 호출 (카카오 알림톡 예정 - 사업자 인증 문제로 보류)
    }
}
