package com.example.coursemanagement.model.projection;

import com.example.coursemanagement.entity.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface CoursePublic {
    Integer getId();

    String getName();

    String getSlug();

    String getDescription();

    String getType();

    String getThumbnail();

    UserPublic getUser();

    List<TopicPublic> getTopics();

    @RequiredArgsConstructor
    class CoursePublicImpl implements CoursePublic {
        @JsonIgnore
        private final Course course;

        @Override
        public Integer getId() {
            return this.course.getId();
        }

        @Override
        public String getName() {
            return this.course.getName();
        }

        @Override
        public String getSlug() {
            return this.course.getSlug();
        }

        @Override
        public String getDescription() {
            return this.course.getDescription();
        }

        @Override
        public String getType() {
            return this.course.getType();
        }

        @Override
        public String getThumbnail() {
            return this.course.getThumbnail();
        }

        @Override
        public UserPublic getUser() {
            return UserPublic.of(this.course.getUser());
        }

        @Override
        public List<TopicPublic> getTopics() {
            return this.course.getTopics()
                    .stream()
                    .map(topic -> TopicPublic.of(topic))
                    .toList();
        }
    }

    static CoursePublic of(Course course) {
        return new CoursePublicImpl(course);
    }
}
