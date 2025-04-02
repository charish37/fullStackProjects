package com.project.two.foodDeliveryWebsite.service;

import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
    // an abstract uploadFile method
    // multipartfile is a spring class used to handle file uploads like images or pdfs from a form.
    // and this method return a string, i.e a url or a confirmation message or an ID of upload file

    // Let’s say a user uploads an image of a food item on the website. The controller layer receives the HTTP request, and calls this service method to:
    //Upload the file (maybe to a local server or cloud like AWS S3)
    //Get back a URL or confirmation string
    //Store that info in the database, or return it to the client
    String uploadFile(MultipartFile file);
}
