package com.demo.reactive.service.query;

import com.demo.reactive.common.EntityNotFoundException;
import com.demo.reactive.dto.result.user.UserDataResult;
import com.demo.reactive.repository.command.user.UserRepository;
import com.demo.reactive.entity.user.UserEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserDataResult> findById(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDataResult result = new UserDataResult();
                    result.setUserId(user.getId());
                    result.setFirstname(user.getFirstname());
                    result.setLastname(user.getLastname());
                    return result;
                }).switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")));
    }

    @Override
    public Mono<UserEntity> save(UserEntity user) {
        return userRepository.save(user);
    }
}
