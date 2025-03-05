package com.demo.reactive.repository.command.user;

import com.demo.reactive.entity.user.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Flux<UserEntity> findAll();
    Mono<UserEntity> findById(Integer id);
    Mono<UserEntity> save(UserEntity user);
}
