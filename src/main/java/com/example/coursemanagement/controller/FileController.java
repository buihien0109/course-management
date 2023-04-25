package com.example.coursemanagement.controller;

import com.example.coursemanagement.entity.FileServer;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // 1. Upload file
    @PostMapping("")
    public ResponseEntity<FileResponse> uploadFile(@ModelAttribute("file") MultipartFile file) {
        FileResponse fileResponse = fileService.uploadFile(file);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }

    // 2. Xem thông tin file
    @GetMapping(value = "{id}")
    public ResponseEntity<byte[]> readFile(@PathVariable Integer id) {
        FileServer fileServer = fileService.getFile(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(fileServer.getType()))
                .body(fileServer.getData());
    }
}
