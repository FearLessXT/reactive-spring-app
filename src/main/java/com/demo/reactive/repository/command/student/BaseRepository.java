package com.demo.reactive.repository.command.student;

import com.demo.reactive.entity.student.StudentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BaseRepository extends ReactiveCrudRepository<StudentEntity, Integer> {
}
