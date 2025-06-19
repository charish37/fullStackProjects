package com.project.two.foodDeliveryWebsite.service;

import com.project.two.foodDeliveryWebsite.entity.FoodEntity;
import com.project.two.foodDeliveryWebsite.io.FoodRequest;
import com.project.two.foodDeliveryWebsite.io.FoodResponse;
import com.project.two.foodDeliveryWebsite.respository.FoodRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private S3Client s3Client; // instance of s3client

    @Autowired
    private FoodRepository foodRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;
    @Override
    public String uploadFile(MultipartFile file) {
       String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1); // extracting filename after last dot
        String key = UUID.randomUUID().toString()+"."+filenameExtension; // create unique key using UUID and filename
        try{
            // creating putobject request to upload files to s3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
//                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            // send put object to s3client using putobject that we generated and converting the body content into the bytes accepted by s3client
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            if(response.sdkHttpResponse().isSuccessful()){
                // if file uploaded successfully, hten it will return a url using bucketname and key generated
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File upload failed");
            }
        } catch(IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured while uploading file");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file){
        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);






























    }

    // converting request to the entity
    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
