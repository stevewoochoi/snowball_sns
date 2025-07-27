package com.snowball.snowball.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.presign.expire-seconds:300}")
    private int expireSeconds;

    public String generatePresignedUploadUrl(String fileName, String contentType) {
        // 파라미터 검증 및 로그
        System.out.println("[S3Service] === Presign URL 생성 요청 ===");
        System.out.println("fileName: " + fileName);
        System.out.println("contentType: " + contentType);

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("fileName이 필요합니다.");
        }
        if (contentType == null || contentType.isEmpty()) {
            throw new IllegalArgumentException("contentType이 필요합니다.");
        }

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(contentType)
                .build();

        System.out.println("[S3Service] Presign 생성 파라미터:");
        System.out.println("  bucket: " + bucket);
        System.out.println("  region: " + region);
        System.out.println("  expireSeconds: " + expireSeconds);

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder ->
                builder.signatureDuration(Duration.ofSeconds(expireSeconds))
                        .putObjectRequest(objectRequest)
        );

        URL presignedUrl = presignedRequest.url();
        System.out.println("[S3Service] presignedUrl: " + presignedUrl);

        presigner.close();
        return presignedUrl.toString();
    }
}