package com.demo.reactive.controller.command;

import com.demo.reactive.dto.result.student.StudentDataResult;
import com.demo.reactive.service.command.StudentService;
import com.demo.reactive.entity.student.StudentEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("register")
    Mono<StudentEntity> save(
            @RequestBody StudentEntity student
    ) {
        return studentService.save(student);
    }

    @GetMapping
    Flux<StudentEntity> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<StudentDataResult>> findById(
            @PathVariable("id") Integer id) {
        Mono<StudentDataResult> resultMono = studentService.findById(id);
        return resultMono.map(ResponseEntity::ok);
    }
}
