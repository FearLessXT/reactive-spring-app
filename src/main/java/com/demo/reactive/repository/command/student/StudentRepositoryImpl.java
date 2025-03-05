package com.demo.reactive.repository.command.student;

import com.demo.reactive.entity.student.StudentEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final BaseRepository baseRepository;

    public StudentRepositoryImpl(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public Flux<StudentEntity> findAll() {
        return baseRepository.findAll().delayElements(Duration.ofSeconds(1));
    }

    @Override
    public Mono<StudentEntity> findById(Integer id) {
        return baseRepository.findById(id);
    }

    @Override
    public Mono<StudentEntity> save(StudentEntity student) {
        return baseRepository.save(student);
    }
}
