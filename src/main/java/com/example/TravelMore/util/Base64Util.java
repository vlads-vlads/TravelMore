package com.example.TravelMore.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {
    public static String encodeToString(byte[] imageBytes) {
        String encodedString = null;
        encodedString = Base64.getEncoder().encodeToString(imageBytes);

        return encodedString;
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void main(String[] args) {
//        TODO: change to interact with the database
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/main/resources/strawberry.png"));
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        String encodedImage = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            encodedImage = encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        img = decodeToImage(encodedImage);
        try {
            File outputfile = new File("src/main/resources/decoded_strawberry.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
