package ru.itis.flavorful_book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.flavorful_book.exception.ImageSaveException;
import ru.itis.flavorful_book.service.ImageService;

import java.util.Map;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/recipe")
    public ResponseEntity<?> uploadRecipeImage(@RequestParam("image") MultipartFile file) {
        try {
            String url = imageService.saveRecipeImage(file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (ImageSaveException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("image") MultipartFile file) {
        try {
            String url = imageService.saveUserAvatar(file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (ImageSaveException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
