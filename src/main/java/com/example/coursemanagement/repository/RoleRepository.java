package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    List<Role> findByIdIn(List<Integer> roleIds);
}