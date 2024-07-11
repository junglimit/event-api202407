package com.study.event.api.event.repository;

import com.study.event.api.event.entity.EmailVerification;
import com.study.event.api.event.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {

    // 유저정보를 통해 인증코드 정보를 탐색 ( FK 를 통해서 찾기, 꼭 필드명으로 쓰기 )

    Optional<EmailVerification> findByEventUser(EventUser eventUser);
}
