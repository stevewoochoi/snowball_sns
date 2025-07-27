package com.snowball.snowball.controller;

import com.snowball.snowball.service.S3Service;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/presign")
    public Map<String, Object> getPresignUrl(@RequestBody PresignRequest request) {
        System.out.println("Presign 요청 fileName=" + request.getFileName() + ", contentType=" + request.getContentType());
        Map<String, Object> result = new HashMap<>();
        result.put("url", s3Service.generatePresignedUploadUrl(request.getFileName(), request.getContentType()));
        return result;
    }

    // PresignRequest DTO
    public static class PresignRequest {
        private String fileName;
        private String contentType;
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }
}