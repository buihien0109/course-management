package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.entity.FileServer;
import com.example.coursemanagement.exception.NotFoundException;
import com.example.coursemanagement.repository.FileServerRepository;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.security.ICurrentUser;
import com.example.coursemanagement.service.FileService;
import com.example.coursemanagement.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileServerRepository fileServerRepository;
    private final ICurrentUser iCurrentUser;

    // Upload file
    @Override
    public FileResponse uploadFile(MultipartFile file) {
        FileUtils.validateFile(file);

        try {
            FileServer fileServer = FileServer.builder()
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .user(iCurrentUser.getUser())
                    .build();
            fileServerRepository.save(fileServer);

            return new FileResponse("/api/files/" + fileServer.getId());
        } catch (IOException e) {
            throw new RuntimeException("Có lỗi xảy ra khi upload file");
        }
    }

    // Lấy thông tin file theo id
    @Override
    public FileServer getFile(Integer id) {
        FileServer fileServer = fileServerRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found image");
                });
        return fileServer;
    }
}
