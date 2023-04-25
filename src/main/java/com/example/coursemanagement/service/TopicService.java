package com.example.coursemanagement.service;

import com.example.coursemanagement.model.projection.TopicPublic;
import com.example.coursemanagement.model.request.UpsertTopicRequest;
import com.example.coursemanagement.util.PageUtil;

import java.util.List;

public interface TopicService {
    List<TopicPublic> getAll();

    PageUtil<TopicPublic> getAll(int page, int size);

    TopicPublic createTopic(UpsertTopicRequest request);

    TopicPublic updateTopic(Integer id, UpsertTopicRequest request);

    void deleteTopic(Integer id);
}
