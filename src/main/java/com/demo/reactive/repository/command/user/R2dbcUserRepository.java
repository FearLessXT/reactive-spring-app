package com.demo.reactive.repository.command.user;

import com.demo.reactive.entity.user.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface R2dbcUserRepository extends ReactiveCrudRepository<UserEntity, Integer> {
}
