package com.clp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }

    @PostConstruct
    public void logPostConstruct () {
        log.info("************* S3 *********");
        log.info("accessKey: {}", accessKey);
        log.info("accessSecret: {}", accessSecret);
        log.info("region: {}", region);
        log.info("************* S3 *********");
    }
}