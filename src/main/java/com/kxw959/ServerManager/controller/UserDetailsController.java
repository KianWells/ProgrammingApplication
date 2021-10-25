package com.kxw959.ServerManager.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kxw959.ServerManager.entity.User;
import com.kxw959.ServerManager.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserDetailsController {
    @Autowired
    UserDetailsRepo userDetailsRepo;

    @GetMapping("/")
    public String health() {
        return "Everything OK";
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody User user){
        return userDetailsRepo.save(user);
    }

    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable("username") String username){
        return userDetailsRepo.getUserByUsername(username);
    }

    @DeleteMapping("/user/{username}")
    public String deleteUser(@PathVariable("username") String username){
        return userDetailsRepo.delete(username);
    }

    @PutMapping("/user/{username}")
    public String updateUser(@PathVariable("username") String username, @RequestBody User user){
        return userDetailsRepo.update(username, user);
    }

}
