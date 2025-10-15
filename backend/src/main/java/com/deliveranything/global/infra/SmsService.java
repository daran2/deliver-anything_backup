package com.deliveranything.global.infra;

public interface SmsService {
  void sendSms(String phoneNumber, String message);
}
