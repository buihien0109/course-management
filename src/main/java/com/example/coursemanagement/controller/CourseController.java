package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.projection.CoursePublic;
import com.example.coursemanagement.model.projection.TopicPublic;
import com.example.coursemanagement.model.projection.UserPublic;
import com.example.coursemanagement.model.request.UpsertCourseRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.service.CourseService;
import com.example.coursemanagement.service.TopicService;
import com.example.coursemanagement.service.UserService;
import com.example.coursemanagement.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final TopicService topicService;
    private final UserService userService;

    // Danh sách View
    @GetMapping("admin/courses")
    public String getListCoursePage(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        PageUtil<CoursePublic> data = courseService.getAll(page, size);
        model.addAttribute("data", data);
        return "admin/course/course-list";
    }

    @GetMapping("admin/courses/create")
    public String getCreateCoursePage(Model model) {
        List<TopicPublic> topicList = topicService.getAll();
        List<UserPublic> userList = userService.getUserByRole("SALE");

        model.addAttribute("topics", topicList);
        model.addAttribute("supporters", userList);

        return "admin/course/course-create";
    }

    @GetMapping("admin/courses/{id}/{slug}")
    public String getDetailCoursePage(@PathVariable Integer id, @PathVariable String slug, Model model) {
        List<TopicPublic> topicList = topicService.getAll();
        List<UserPublic> userList = userService.getUserByRole("SALE");
        CoursePublic course = courseService.getCourseById(id);

        model.addAttribute("topics", topicList);
        model.addAttribute("supporters", userList);
        model.addAttribute("course", course);
        return "admin/course/course-edit";
    }

    // Danh sách API
    @PostMapping("api/admin/courses")
    public ResponseEntity<?> createCourse(@RequestBody UpsertCourseRequest request) {
        CoursePublic course = courseService.createCourse(request);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping("api/admin/courses/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable int id, @RequestBody UpsertCourseRequest request) {
        CoursePublic course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("api/admin/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("api/admin/courses/{id}/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail(@ModelAttribute("file") MultipartFile file, @PathVariable Integer id) {
        FileResponse fileResponse = courseService.uploadThumbnail(id, file);
        return ResponseEntity.ok(fileResponse);
    }
}
