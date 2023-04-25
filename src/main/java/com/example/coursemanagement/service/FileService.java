package com.example.coursemanagement.service;

import com.example.coursemanagement.entity.FileServer;
import com.example.coursemanagement.model.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileResponse uploadFile(MultipartFile file);

    FileServer getFile(Integer id);
}
