package com.demo.reactive.service.command;

import com.demo.reactive.dto.result.student.StudentDataResult;
import com.demo.reactive.entity.student.StudentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Flux<StudentEntity> findAll();
    Mono<StudentDataResult> findById(Integer id);
    Mono<StudentEntity> save(StudentEntity student);
}
