package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.projection.TopicPublic;
import com.example.coursemanagement.model.request.UpsertTopicRequest;
import com.example.coursemanagement.service.TopicService;
import com.example.coursemanagement.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    // Danh sách View
    @GetMapping("admin/topics")
    public String getListTopicPage(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        PageUtil<TopicPublic> data = topicService.getAll(page, size);
        model.addAttribute("data", data);
        return "admin/topic/topic-list";
    }

    // Danh sách API
    // 3. Tạo mới category
    @PostMapping("api/admin/topics")
    public ResponseEntity<?> createTopic(@RequestBody UpsertTopicRequest request) {
        TopicPublic topic = topicService.createTopic(request);
        return new ResponseEntity<>(topic, HttpStatus.CREATED);
    }

    // 4. Cập nhật category
    @PutMapping("api/admin/topics/{id}")
    public ResponseEntity<?> updateTopic(@RequestBody UpsertTopicRequest request, @PathVariable Integer id) {
        TopicPublic topic = topicService.updateTopic(id, request);
        return ResponseEntity.ok(topic);
    }

    // 5. Xóa category
    @DeleteMapping("api/admin/topics/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
