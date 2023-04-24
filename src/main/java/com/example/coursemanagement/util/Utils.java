package com.example.coursemanagement.util;

import com.example.coursemanagement.model.projection.TopicPublic;

import java.util.List;

public class Utils {
    public static String generateTopicString(List<TopicPublic> topics) {
        if (topics.size() == 0) {
            return "";
        }

        // Lấy ra Listname của Category
        List<String> topicNames = topics.stream()
                .map(topicPublic -> topicPublic.getName())
                .toList();

        // Chuyển List -> Array
        String[] itemsArray = new String[topicNames.size()];
        itemsArray = topicNames.toArray(itemsArray);

        // Join Array -> String
        return String.join(", ", itemsArray);
    }
}
