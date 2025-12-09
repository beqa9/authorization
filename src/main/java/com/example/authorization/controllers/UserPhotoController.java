package com.example.authorization.controllers;

import com.example.authorization.entities.UserImage;
import com.example.authorization.minio.MinioService;
import com.example.authorization.repositories.UserImageRepository;
import com.example.authorization.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserPhotoController {

    private final MinioService minioService;
    private final UserSessionRepository sessionRepo;
    private final UserImageRepository imageRepo;


    @PostMapping("/{userId}/photos/upload")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long userId,
                                              @RequestParam MultipartFile file,
                                              @RequestHeader("Authorization") String authHeader) throws Exception {
        Long sessionId = sessionRepo.findTopByUserIdOrderByLastSeenDesc(userId)
                .map(s -> s.getId())
                .orElseThrow(() -> new RuntimeException("No active session found"));

        String objectName = minioService.uploadImage(file, sessionId);

        return ResponseEntity.ok(objectName);
    }


    @GetMapping("/{userId}/photos")
    public List<UserImage> listUserPhotos(@PathVariable Long userId) {
        Long sessionId = sessionRepo.findTopByUserIdOrderByLastSeenDesc(userId)
                .map(s -> s.getId())
                .orElseThrow(() -> new RuntimeException("No active session found"));

        return imageRepo.findBySessionId(sessionId);
    }


    @GetMapping("/{userId}/photos/download/{objectName}")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable Long userId,
                                                @PathVariable String objectName) throws Exception {
        InputStream stream = minioService.downloadImage(objectName);
        byte[] content = stream.readAllBytes();
        stream.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + objectName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }
}