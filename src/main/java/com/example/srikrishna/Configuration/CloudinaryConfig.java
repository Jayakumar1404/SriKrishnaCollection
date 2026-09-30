package com.example.srikrishna.Configuration;



import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(
                "cloudinary://" +
                System.getenv("CLOUDINARY_API_KEY") +
                ":" +
                System.getenv("CLOUDINARY_API_SECRET") +
                "@" +
                System.getenv("CLOUDINARY_CLOUD_NAME")
        );
    }
}
