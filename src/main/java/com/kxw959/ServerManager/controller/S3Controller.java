package com.kxw959.ServerManager.controller;

import com.kxw959.ServerManager.repository.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Controller {
    @Autowired
    private S3BucketService service;

    @PostMapping("/file/upload")
    private ResponseEntity<String> uploadFile(@RequestBody MultipartFile file){
        return new ResponseEntity<String>(service.uploadFile(file, "jpa2021_"), HttpStatus.OK);
    }

    @PostMapping("/file/upload/{username}")
    private ResponseEntity<String> uploadStudentFile(@RequestBody MultipartFile file, @PathVariable String username){
        return new ResponseEntity<String>(service.uploadFile(file, username+"/jpa2021_"), HttpStatus.OK);
    }

    @GetMapping("/file/download/{fileName}")
    private ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName){
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/file/download/{username}/{fileName}")
    private ResponseEntity<ByteArrayResource> downloadUsernameFile(@PathVariable String username, @PathVariable String fileName){
        byte[] data = service.downloadFile(username+"/"+fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/file/delete/{fileName}")
    private ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return new ResponseEntity<String>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
