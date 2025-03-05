package com.demo.reactive.controller.query;

import com.demo.reactive.dto.result.user.UserDataResult;
import com.demo.reactive.service.query.UserService;
import com.demo.reactive.entity.user.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    Flux<UserEntity> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserDataResult>> getUserById(
            @PathVariable("id")
            Integer id
    ) {
        Mono<UserDataResult> result =  userService.findById(id);
        return result.map(ResponseEntity::ok);
    }
}
