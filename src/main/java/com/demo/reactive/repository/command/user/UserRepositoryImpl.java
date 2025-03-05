package com.demo.reactive.repository.command.user;

import com.demo.reactive.entity.user.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final R2dbcUserRepository repository;

    public UserRepositoryImpl(R2dbcUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<UserEntity> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Mono<UserEntity> save(UserEntity user) {
        return repository.save(user);
    }
}
