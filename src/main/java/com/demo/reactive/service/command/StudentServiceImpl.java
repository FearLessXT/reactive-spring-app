package com.demo.reactive.service.command;

import com.demo.reactive.common.EntityNotFoundException;
import com.demo.reactive.dto.result.student.StudentDataResult;
import com.demo.reactive.repository.command.student.StudentRepository;
import com.demo.reactive.entity.student.StudentEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Flux<StudentEntity> findAll() {
        return studentRepository.findAll()
                .onErrorResume(EntityNotFoundException.class,
                e -> Mono.error(new EntityNotFoundException(e.getMessage())));
    }

    @Override
    public Mono<StudentDataResult> findById(Integer id) {
        return studentRepository.findById(id)
                .map(studentEntity -> {
                    StudentDataResult result = new StudentDataResult();
                    result.setStudentId(studentEntity.getId());
                    result.setFirstname(studentEntity.getFirstname());
                    result.setLastname(studentEntity.getLastname());
                    result.setAge(studentEntity.getAge());
                    return result;
                }).switchIfEmpty(Mono.error(new EntityNotFoundException("Student with id " + id + " not found")));
    }

    @Override
    public Mono<StudentEntity> save(StudentEntity student) {
        return studentRepository.save(student);
    }
}
