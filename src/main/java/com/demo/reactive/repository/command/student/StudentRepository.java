package com.demo.reactive.repository.command.student;

import com.demo.reactive.entity.student.StudentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository{
    Flux<StudentEntity> findAll();
    Mono<StudentEntity> findById(Integer id);
    Mono<StudentEntity> save(StudentEntity student);
}
