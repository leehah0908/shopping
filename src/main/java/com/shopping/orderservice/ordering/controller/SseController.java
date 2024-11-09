package com.shopping.orderservice.ordering.controller;

import com.shopping.orderservice.common.auth.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class SseController {

    // 구독을 요청한 각 사용자의 이메일을 키로 emitter 객체를 저장
    // ConcurrentHashMap: 멀티 스레드 기반의 HashMap (일반 HashMap은 싱글 스레드 기반)
    Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal TokenUserInfo userInfo) {
        SseEmitter emitter = new SseEmitter(1440 * 60 * 1000L); // 알림 서비스 구현 핵심 객체

        String email = userInfo.getEmail();
        emitters.put(email, emitter); // 이메일을 키로 emitter 저장

        log.info("subscribe email: {}", email);

        emitter.onCompletion(() -> emitters.remove(email)); // 클라이언트(관리자)와의 연결이 끊어지면 맵에서 삭제
        emitter.onTimeout(() -> emitters.remove(email)); // 수명이 다 됐으면 맵에서 삭제

        // 연결 성공 메세지
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Success Connect!"));

            // 30초마다 heartbeat 메시지를 전송하여 연결 유지
            // 클라이언트에서 사용하는 EventSourcePolyfill이 45초 동안 활동이 없으면 자동으로 연결을 종료함
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("heartbeat")
                            .data("keep-alive"));
                } catch (IOException e) {
                    emitters.remove(email);
                    System.out.println("Failed to send heartbeat, removing emitter for email: " + email);
                }
            }, 30, 30, TimeUnit.SECONDS); // 30초마다 heartbeat 메시지 전송

        } catch (IOException e) {
            emitters.remove(email);
        }
        return emitter;
    }
}
