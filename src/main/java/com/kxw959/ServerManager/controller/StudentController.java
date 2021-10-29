package com.kxw959.ServerManager.controller;

import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.repository.StudentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    StudentDetailsRepo studentDetailsRepo;

    @PostMapping("/student")
    public Student saveStudent(@RequestBody Student student){
        return studentDetailsRepo.save(student);
    }

    @GetMapping("/student/{username}")
    public Student getStudentByUsername(@PathVariable("username") String username){
        return studentDetailsRepo.getStudentByUsername(username);
    }

    @DeleteMapping("/student/{username}")
    public String deleteStudent(@PathVariable("username") String username){
        return studentDetailsRepo.delete(username);
    }

    @PutMapping("/student/{username}")
    public String updateStudent(@PathVariable("username") String username, @RequestBody Student student){
        return studentDetailsRepo.update(username, student);
    }

    @GetMapping("/student/class/{className}")
    public List<Student> getStudentsByClass(@PathVariable("className") String className){
        return studentDetailsRepo.getUsersByClass(className);
    }
}
