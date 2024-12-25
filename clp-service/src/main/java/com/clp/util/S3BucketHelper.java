package com.clp.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class S3BucketHelper {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    @Value(("${cloud.aws.url.expiration}"))
    private Long expirationMillis;


    public String uploadToS3(MultipartFile multipartFile, String folderName) {
        try {
            String objectKey = folderName + "/"+ LocalDateTime.now() + multipartFile.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest(bucketName, objectKey, multipartFile.getInputStream(), new ObjectMetadata()));
            return objectKey;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }

    public String getUrl(String objectKey, String fileType) {
        ResponseHeaderOverrides headers = new ResponseHeaderOverrides()
                .withContentDisposition("inline")
                .withContentType(fileType);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                        .withResponseHeaders(headers);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public void deleteFromS3(String objectKey, String imageType) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        log.info("Deleted image {} from bucket {}", imageType, bucketName);
    }

    public void updateImage(String objectKey, String imageType, MultipartFile file) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, objectKey, file.getInputStream(), new ObjectMetadata()));
        } catch (IOException e) {
            log.error("Failed to update image {} from bucket {}", file.getOriginalFilename(), bucketName, e);
            throw new RuntimeException("Error updating image to S3", e);
        }
        log.info("Updated image {} from bucket {}", file.getOriginalFilename(), bucketName);
    }

    public S3ObjectInputStream getContentFile(String objectKey,String fileType){
        return amazonS3.getObject(bucketName,objectKey).getObjectContent();
    }
}
