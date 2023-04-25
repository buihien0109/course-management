package com.example.coursemanagement.service;

import com.example.coursemanagement.model.projection.CoursePublic;
import com.example.coursemanagement.model.request.UpsertCourseRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.util.PageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    PageUtil<CoursePublic> getAll(int page, int size);

    List<CoursePublic> getAll(String topic, String name);

    List<CoursePublic> getCoursesByType(String type, String topic, String name);

    CoursePublic getCourseById(Integer id);

    CoursePublic createCourse(UpsertCourseRequest request);

    CoursePublic updateCourse(int id, UpsertCourseRequest request);

    void deleteCourse(Integer id);

    FileResponse uploadThumbnail(Integer id, MultipartFile file);
}
