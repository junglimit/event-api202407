package com.study.event.api.event.repository;

import com.study.event.api.event.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventUserRepository extends JpaRepository<EventUser, String> {

    // query method로 Jpql 생성 , By 다음에는 EventUser 의 필드명으로 써야함
    boolean existsByEmail(String email);

    Optional<EventUser> findByEmail (String email);
}
