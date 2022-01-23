package com.kxw959.ServerManager.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3BucketService {
    private final String bucketName = "java-programming-application-bucket";

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file, String prefix){
        File fileObject = convertMultipartToFile(file);
        String fileName = prefix+file.getOriginalFilename();
        s3Client.putObject(bucketName, fileName, fileObject);
        fileObject.delete();
        return fileName;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream is = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);
        return "Deleted";
    }

    private File convertMultipartToFile(MultipartFile mFile){
        String path = mFile.getOriginalFilename();
        File file;
        if(path!=null)
            file = new File(path);
        else file = new File("");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(mFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
