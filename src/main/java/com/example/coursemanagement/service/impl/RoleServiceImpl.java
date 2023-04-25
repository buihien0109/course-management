package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.model.projection.RolePublic;
import com.example.coursemanagement.repository.RoleRepository;
import com.example.coursemanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    // Lấy danh sách role
    @Override
    public List<RolePublic> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RolePublic.of(role))
                .toList();
    }
}
