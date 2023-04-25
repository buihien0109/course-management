package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.entity.Topic;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.NotFoundException;
import com.example.coursemanagement.model.projection.TopicPublic;
import com.example.coursemanagement.repository.TopicRepository;
import com.example.coursemanagement.model.request.UpsertTopicRequest;
import com.example.coursemanagement.service.TopicService;
import com.example.coursemanagement.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    // Lấy danh sách tất cả topic
    @Override
    public List<TopicPublic> getAll() {
        List<Topic> topicList = topicRepository.findAll();
        return topicList.stream()
                .map(topic -> TopicPublic.of(topic))
                .toList();
    }

    // Lấy danh sách tất cả topic có phân trang
    @Override
    public PageUtil<TopicPublic> getAll(int page, int size) {
        Page<Topic> pageInfo = topicRepository.findAll(PageRequest.of(page - 1, size));

        return new PageUtil<>(
                pageInfo.getContent().stream()
                        .map(topic -> TopicPublic.of(topic)).toList(),
                pageInfo.getPageable().getPageNumber() + 1,
                pageInfo.getPageable().getPageSize(),
                IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList(),
                pageInfo.getTotalElements(),
                pageInfo.isFirst(),
                pageInfo.isLast()
        );
    }

    // Tạo topic
    @Override
    public TopicPublic createTopic(UpsertTopicRequest request) {
        if (request.getName() == null || request.getName().equals("")) {
            throw new BadRequestException("Name is required");
        }

        if (topicRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category is exist");
        }

        Topic topic = new Topic();
        topic.setName(request.getName());

        topicRepository.save(topic);
        return TopicPublic.of(topic);
    }

    // Cập nhật topic
    @Override
    public TopicPublic updateTopic(Integer id, UpsertTopicRequest request) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found topic with id = " + id);
        });

        if (request.getName() == null || request.getName().equals("")) {
            throw new BadRequestException("Name is required");
        }

        if (topicRepository.findByName(request.getName()).isPresent()
                && !topicRepository.findByName(request.getName()).get().getName().equals(topic.getName())) {
            throw new BadRequestException("Category is exist");
        }

        topic.setName(request.getName());
        topicRepository.save(topic);
        return TopicPublic.of(topic);
    }

    // Xóa topic
    @Override
    public void deleteTopic(Integer id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found topic with id = " + id);
        });

        topicRepository.delete(topic);
    }
}
