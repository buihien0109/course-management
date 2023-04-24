package com.example.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.coursemanagement.entity.Topic;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByIdIn(List<Integer> ids);


    Topic getTopicsByNameContaining(String name);

    Optional<Topic> findByName(String name);
}