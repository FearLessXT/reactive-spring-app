package com.demo.reactive.service.query;

import com.demo.reactive.dto.result.user.UserDataResult;
import com.demo.reactive.entity.user.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserEntity> findAll();
    Mono<UserDataResult> findById(Integer id);
    Mono<UserEntity> save(UserEntity user);
}
