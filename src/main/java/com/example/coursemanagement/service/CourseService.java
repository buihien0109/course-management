package com.example.coursemanagement.service;

import com.example.coursemanagement.model.projection.CoursePublic;
import com.example.coursemanagement.model.request.UpsertCourseRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Topic;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.NotFoundException;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.TopicRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.util.PageUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final Slugify slugify;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final FileService fileService;

    // Lấy danh sách tất cả khóa học có phân trang
    public PageUtil<CoursePublic> getAll(int page, int size) {
        Page<Course> pageInfo = courseRepository.findAll(PageRequest.of(page - 1, size));

        return new PageUtil<>(
                pageInfo.getContent().stream()
                        .map(course -> CoursePublic.of(course)).toList(),
                pageInfo.getPageable().getPageNumber() + 1,
                pageInfo.getPageable().getPageSize(),
                IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList(),
                pageInfo.getTotalElements(),
                pageInfo.isFirst(),
                pageInfo.isLast()
        );
    }

    // Lấy danh sách tất cả khóa học theo bộ lọc (topic, name)
    public List<CoursePublic> getAll(String topic, String name) {
        List<Course> courseList = courseRepository.findAll();
        List<Course> courseFilterList = getCoursesFilter(topic, name, courseList);
        return courseFilterList.stream()
                .map(course -> CoursePublic.of(course))
                .toList();
    }

    // Lấy danh sách khóa học theo type theo bộ lọc (topic, name)
    public List<CoursePublic> getCoursesByType(String type, String topic, String name) {
        List<Course> courseList = courseRepository.getCourseByType(type);
        List<Course> courseFilterList = getCoursesFilter(topic, name, courseList);
        return courseFilterList.stream()
                .map(course -> CoursePublic.of(course))
                .toList();
    }

    // Lọc danh sách khóa học theo type, topic, name
    private List<Course> getCoursesFilter(String topic, String name, List<Course> courses) {
        if (!topic.trim().equals("")) {
            Topic topicObj = topicRepository.getTopicsByNameContaining(topic);
            courses = courses
                    .stream()
                    .filter(course -> course.getTopics().contains(topicObj))
                    .collect(Collectors.toList());
        }
        if (!name.trim().equals("")) {
            courses = courses
                    .stream()
                    .filter(course -> course.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return courses;
    }

    // Lấy thông tin khóa học theo id
    public CoursePublic getCourseById(Integer id) {
        Course course =  courseRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found course with id = " + id);
        });
        return CoursePublic.of(course);
    }

    // Tạo khóa học
    public CoursePublic createCourse(UpsertCourseRequest request) {
        // Tìm kiếm user
        User user = userRepository.findById(request.getSupporterId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found user");
                });

        // Tìm kiếm danh sách topic
        List<Topic> topics = topicRepository.findByIdIn(request.getTopics());

        Course course = Course.builder()
                .name(request.getName())
                .slug(slugify.slugify(request.getName()))
                .description(request.getDescription())
                .type(request.getType())
                .topics(topics)
                .user(user)
                .build();

        courseRepository.save(course);
        return CoursePublic.of(course);
    }

    // Cập nhật thông tin khóa học
    public CoursePublic updateCourse(int id, UpsertCourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found course with id = " + id);
        });

        // Tìm kiếm user
        User user = userRepository.findById(request.getSupporterId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found user");
                });

        // Tìm kiếm danh sách topic
        List<Topic> topics = topicRepository.findByIdIn(request.getTopics());

        course.setName(request.getName());
        course.setSlug(slugify.slugify(request.getName()));
        course.setDescription(request.getDescription());
        course.setType(request.getType());
        course.setUser(user);
        course.setTopics(topics);

        courseRepository.save(course);
        return CoursePublic.of(course);
    }

    // Xóa khóa học
    public void deleteCourse(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found course with id = " + id);
        });

        courseRepository.delete(course);
    }

    // Thay đổi ảnh khóa học
    public FileResponse uploadThumbnail(Integer id, MultipartFile file) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found course with id = " + id);
        });

        // Upload file
        FileResponse fileResponse = fileService.uploadFile(file);

        course.setThumbnail(fileResponse.getUrl());
        courseRepository.save(course);

        return fileResponse;
    }
}
