package com.app.mydoc.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String upload(MultipartFile file, String folder);

    void delete(String fileUrl);
}
