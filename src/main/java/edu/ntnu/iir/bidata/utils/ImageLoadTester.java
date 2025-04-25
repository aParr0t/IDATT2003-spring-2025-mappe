package edu.ntnu.iir.bidata.utils;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for loading images in JavaFX from various sources
 */
public class ImageLoadTester {
    // Cache to store loaded images using path as key
    private static final ConcurrentHashMap<String, Image> imageCache = new ConcurrentHashMap<>();

    /**
     * Attempts to load an image using multiple methods
     * @param imagePath The path to the image
     * @return The loaded image or null if all methods fail
     */
    public static Image attemptLoadImage(String imagePath) {
        if (imagePath == null) {
            return null;
        }

        // Check if image is already in cache
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

        // Normalize path
        String resourcePath = imagePath;
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }
        
        Image image = null;
        
        // Method 1: Try as class resource (most reliable for packaged apps)
        try {
            var classLoader = ImageLoadTester.class.getClassLoader();
            var inputStream = classLoader.getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                image = new Image(inputStream);
                if (!image.isError()) {
                    imageCache.put(imagePath, image);
                    return image;
                }
            }
        } catch (Exception ignored) {}
        
        // Method 2: Try as file URL
        try {
            image = new Image("file:" + imagePath);
            if (!image.isError()) {
                imageCache.put(imagePath, image);
                return image;
            }
        } catch (Exception ignored) {}
        
        // Method 3: Try with direct path
        try {
            image = new Image(imagePath);
            if (!image.isError()) {
                imageCache.put(imagePath, image);
                return image;
            }
        } catch (Exception ignored) {}
        
        // Method 4: Try with absolute file path via FileInputStream
        try {
            if (Files.exists(Paths.get(imagePath))) {
                image = new Image(new FileInputStream(imagePath));
                if (!image.isError()) {
                    imageCache.put(imagePath, image);
                    return image;
                }
            }
        } catch (Exception ignored) {}
        
        // Method 5: Check in resources directory
        try {
            var resourcesPath = Paths.get("src/main/resources", resourcePath);
            if (Files.exists(resourcesPath)) {
                image = new Image(new FileInputStream(resourcesPath.toFile()));
                if (!image.isError()) {
                    imageCache.put(imagePath, image);
                    return image;
                }
            }
        } catch (Exception ignored) {}
        
        return null;
    }

    /**
     * Clears the image cache to free memory
     */
    public static void clearCache() {
        imageCache.clear();
    }
} 