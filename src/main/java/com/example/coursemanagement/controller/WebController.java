package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.projection.CoursePublic;
import com.example.coursemanagement.model.projection.TopicPublic;
import com.example.coursemanagement.service.CourseService;
import com.example.coursemanagement.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final TopicService topicService;
    private final CourseService courseService;

    @GetMapping("khoa-hoc")
    public String getListCoursePage(
            Model model,
            @RequestParam(required = false, defaultValue = "") String topic,
            @RequestParam(required = false, defaultValue = "") String name) {
        List<TopicPublic> topicList = topicService.getAll();
        List<CoursePublic> courseList = courseService.getAll(topic, name);

        model.addAttribute("topics", topicList);
        model.addAttribute("courses", courseList);

        return "web/course-list";
    }

    @GetMapping("khoa-hoc/online")
    public String getOnlineCoursePage(
            Model model,
            @RequestParam(required = false, defaultValue = "") String topic,
            @RequestParam(required = false, defaultValue = "") String name) {
        List<TopicPublic> topicList = topicService.getAll();
        List<CoursePublic> courseList = courseService.getCoursesByType("online", topic, name);

        model.addAttribute("topics", topicList);
        model.addAttribute("courses", courseList);
        return "web/course-online-list";
    }

    @GetMapping("khoa-hoc/onlab")
    public String getOnlabCoursePage(
            Model model,
            @RequestParam(required = false, defaultValue = "") String topic,
            @RequestParam(required = false, defaultValue = "") String name) {
        List<TopicPublic> topicList = topicService.getAll();
        List<CoursePublic> courseList = courseService.getCoursesByType("onlab", topic, name);

        model.addAttribute("topics", topicList);
        model.addAttribute("courses", courseList);

        return "web/course-onlab-list";
    }

    @GetMapping("khoa-hoc/{id}/{slug}")
    public String getDetailCoursePage(
            @PathVariable int id,
            @PathVariable String slug,
            Model model) {
        CoursePublic course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "web/detail";
    }
}
