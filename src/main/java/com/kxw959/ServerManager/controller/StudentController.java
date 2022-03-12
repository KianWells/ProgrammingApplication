package com.kxw959.ServerManager.controller;

import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.StudentTask;
import com.kxw959.ServerManager.entity.Students;
import com.kxw959.ServerManager.repository.StudentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
        return studentDetailsRepo.getStudentByID(username);
    }

    @DeleteMapping("/student/{username}")
    public String deleteStudent(@PathVariable("username") String username){
        return studentDetailsRepo.delete(username);
    }

    @PutMapping("/student/{username}")
    public String updateStudent(@PathVariable("username") String username, @RequestBody Student student) throws IOException {
        return studentDetailsRepo.update(username, student);
    }

    @GetMapping("/student/class/{className}")
    public Students getStudentsByClass(@PathVariable("className") String className){
        return studentDetailsRepo.getUsersByClass(className);
    }

    @PostMapping("student/class/{className}")
    public String uploadTask(@PathVariable("className") String className, @RequestBody StudentTask task) throws IOException {
        return studentDetailsRepo.addTask(className, task);
    }
}
