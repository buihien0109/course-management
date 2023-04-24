package com.example.coursemanagement;

import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Role;
import com.example.coursemanagement.entity.Topic;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.RoleRepository;
import com.example.coursemanagement.repository.TopicRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class InitDataTests {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Slugify slugify;


    @Test
    void save_roles() {
        List<Role> roles = List.of(
                new Role(null, "ADMIN"),
                new Role(null, "USER"),
                new Role(null, "SALE")
        );

        roleRepository.saveAll(roles);
    }

    @Test
    void save_topic() {
        List<Topic> topicList = List.of(
                new Topic(null, "backend"),
                new Topic(null, "frontend"),
                new Topic(null, "devops"),
                new Topic(null, "mobile"),
                new Topic(null, "database")
        );
        topicRepository.saveAll(topicList);
    }

    @Test
    void save_user() {
        Role userRole = roleRepository.findByName("USER").orElse(null);
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        Role saleRole = roleRepository.findByName("SALE").orElse(null);

        List<User> users = List.of(
                new User(null, "Bùi Hiên" ,"hien@gmail.com", "0344005816", passwordEncoder.encode("111"), null, List.of(adminRole, userRole)),
                new User(null, "Đức Thịnh", "thinh@gmail.com", "0988888888", passwordEncoder.encode("111"), null, List.of(userRole, saleRole)),
                new User(null, "Phạm Mẫn", "man@gmail.com", "0977777777", passwordEncoder.encode("111"), null, List.of(saleRole, userRole)),
                new User(null, "Thanh Hương", "huong@gmail.com", "0966666666", passwordEncoder.encode("111"), null, List.of(saleRole, userRole))
        );

        userRepository.saveAll(users);
    }

    @Test
    void save_course() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> userList = userRepository.findByRoles_NameIgnoreCase("SALE");
        List<Topic> topicList = topicRepository.findAll();

        for (int i = 0; i < 20; i++) {
            // Random 1 user
            User rdUser = userList.get(rd.nextInt(userList.size()));

            // Random 1 ds category
            List<Topic> rdTopicList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Topic rdTopic = topicList.get(rd.nextInt(topicList.size()));
                if(!rdTopicList.contains(rdTopic)) {
                    rdTopicList.add(rdTopic);
                }
            }

            // Tạo course
            String name = faker.book().title();
            Course course = Course.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .description(faker.lorem().sentence(15))
                    .type(rd.nextInt(2) == 0 ? "online" : "onlab")
                    .user(rdUser)
                    .topics(rdTopicList)
                    .build();

            courseRepository.save(course);
        }
    }
}
