package com.mySpring.demo.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageTransformer {
    
    public static byte[] encodeImageToBase64(String imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        // String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        return imageBytes;
    }

    public static byte[] decodeBase64ToImage(String encodedImage) {
        byte[] decodedImage = Base64.getDecoder().decode(encodedImage);
        return decodedImage;
    }
}