package com.example.authorization.minio;

import com.example.authorization.entities.UserImage;
import com.example.authorization.repositories.UserImageRepository;
import io.minio.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties props;
    private final UserImageRepository imageRepo;

    public MinioService(MinioClient minioClient, MinioProperties props, UserImageRepository imageRepo) {
        this.minioClient = minioClient;
        this.props = props;
        this.imageRepo = imageRepo;
    }

    private void createBucketIfNotExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(props.getBucket()).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(props.getBucket()).build());
        }
    }

    // Upload image
    public String uploadImage(MultipartFile file, Long sessionId) throws Exception {
        createBucketIfNotExists();

        String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("sessionId", sessionId.toString());
        metadata.put("fileName", file.getOriginalFilename());
        metadata.put("uploadTime", OffsetDateTime.now().toString());
        metadata.put("contentType", file.getContentType());

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(props.getBucket())
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .userMetadata(metadata)
                .build()
        );

        UserImage img = new UserImage();
        img.setSessionId(sessionId);
        img.setFileName(file.getOriginalFilename());
        img.setObjectName(objectName);
        img.setBucket(props.getBucket());
        img.setUploadedAt(OffsetDateTime.now());
        imageRepo.save(img);

        // Also create a log in MinIO for this upload
        uploadLog("Uploaded image: " + file.getOriginalFilename(), sessionId);

        return objectName;
    }

    // Download image
    public InputStream downloadImage(String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(props.getBucket())
                .object(objectName)
                .build()
        );
    }

    // Upload session log
    public void uploadLog(String message, Long sessionId) throws Exception {
        createBucketIfNotExists();

        String logObjectName = "logs/" + sessionId + "-" + OffsetDateTime.now().toString() + ".txt";
        InputStream stream = new ByteArrayInputStream(message.getBytes());

        Map<String, String> metadata = new HashMap<>();
        metadata.put("sessionId", sessionId.toString());
        metadata.put("logTime", OffsetDateTime.now().toString());

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(props.getBucket())
                .object(logObjectName)
                .stream(stream, stream.available(), -1)
                .userMetadata(metadata)
                .build()
        );
        stream.close();
    }
}