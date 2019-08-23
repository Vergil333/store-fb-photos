package com.machava.demo.managers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FbApiManager {

    public static byte[] convertImageToByte(String imageUrl) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage originalImage = ImageIO.read(new URL(imageUrl));
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not download image: " + e +
                    "Given url: " + imageUrl);
        }
    }

}
