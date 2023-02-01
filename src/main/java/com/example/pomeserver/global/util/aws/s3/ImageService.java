package com.example.pomeserver.global.util.aws.s3;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    public static String s3Buket;

    @Value("${aws.s3.buket}")
    public void setS3Buket(String value){
        s3Buket = value;
    }

    public static String getImageURL(String imageKey){
        return s3Buket+imageKey;
    }

}
