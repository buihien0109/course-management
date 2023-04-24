package com.example.coursemanagement.util;

import com.example.coursemanagement.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUtils {
    // Validate file
    public static void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // Tên file không được trống
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        // Type file có nằm trong ds cho phép hay không
        // avatar.png, image.jpg => png, jpg
        String fileExtension = getFileExtension(fileName);
        if (!checkFileExtension(fileExtension)) {
            throw new BadRequestException("Type file không hợp lệ");
        }

        // Kích thước size có trong phạm vi cho phép không
        double fileSize = (double) (file.getSize() / 1_048_576);
        if (fileSize > 2) {
            throw new BadRequestException("Size file không được vượt quá 2MB");
        }
    }

    public static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }

    public static boolean checkFileExtension(String fileExtension) {
        List<String> fileExtensions = List.of("png", "jpg", "jpeg");
        return fileExtensions.contains(fileExtension);
    }
}
