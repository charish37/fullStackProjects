package com.project.two.foodDeliveryWebsite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.two.foodDeliveryWebsite.io.FoodRequest;
import com.project.two.foodDeliveryWebsite.io.FoodResponse;
import com.project.two.foodDeliveryWebsite.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {


    private final FoodService foodService;

    // here the addFood will get two parameters food and file, using object mapper we convert json string into object and pass it as a request to the foodservice addFood method
    // and this will return a response which will be returned by controller addFoood method.

    @PostMapping
    // mehod accept a multipart request of food and file
    public FoodResponse addFood(@RequestPart("food") String foodString, @RequestPart("file") MultipartFile file){
        // converts an object into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            // converting foodString into foodRquest using object mapper
           request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch(JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON format");
        }
        FoodResponse  response= foodService.addFood(request,file);
        return response;
    }
}
