package com.project.two.foodDeliveryWebsite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.stream.DoubleStream;

// this config annotation tells spring that this class contains a bean which are to be managed by a container.
@Configuration
public class AWSConfig {
    @Value("${aws.access.key}") // value is used to inject a value into variable either from env variables or from application properties or yml file
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    // Here in the below we create S3client bean object
    // So, any class that needs this S3Client bean can be autowired
    // the result is an instance of S3Client can be used to upload, download,list or delete files in s3
    @Bean
    public S3Client s3Client(){
        return S3Client.builder() // builder pattern helps to create complex objects or beans
               .region(Region.of(region))
               .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
               .build();
    }

}
