package com.example.coursemanagement.model.projection;

import com.example.coursemanagement.entity.Topic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;

public interface TopicPublic {
    Integer getId();

    String getName();

    @RequiredArgsConstructor
    class TopicPublicImpl implements TopicPublic {
        @JsonIgnore
        private final Topic topic;

        @Override
        public Integer getId() {
            return this.topic.getId();
        }

        @Override
        public String getName() {
            return this.topic.getName();
        }
    }

    static TopicPublic of(Topic topic) {
        return new TopicPublicImpl(topic);
    }
}
