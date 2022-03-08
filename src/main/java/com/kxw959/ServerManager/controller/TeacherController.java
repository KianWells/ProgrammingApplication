package com.kxw959.ServerManager.controller;

import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.Teacher;
import com.kxw959.ServerManager.repository.StudentDetailsRepo;
import com.kxw959.ServerManager.repository.TeacherDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TeacherController {
    @Autowired
    TeacherDetailsRepo teacherDetailsRepo;

    @PostMapping("/teacher")
    public Teacher saveStudent(@RequestBody Teacher teacher) throws IOException {
        return teacherDetailsRepo.save(teacher);
    }

    @GetMapping("/teacher/{username}")
    public Teacher getTeacherByUsername(@PathVariable("username") String username){
        return teacherDetailsRepo.getTeacherByUsername(username);
    }

    @DeleteMapping("/teacher/{username}")
    public String deleteTeacher(@PathVariable("username") String username){
        return teacherDetailsRepo.delete(username);
    }

    @PutMapping("/teacher/{username}")
    public String updateTeacher(@PathVariable("username") String username, @RequestBody Teacher teacher){
        return teacherDetailsRepo.update(username, teacher);
    }
}
