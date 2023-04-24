package com.example.coursemanagement.service;

import com.example.coursemanagement.entity.Role;
import com.example.coursemanagement.model.projection.RolePublic;
import com.example.coursemanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // Lấy danh sách role
    public List<RolePublic> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RolePublic.of(role))
                .toList();
    }
}
