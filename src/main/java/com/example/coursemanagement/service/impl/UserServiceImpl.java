package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.entity.Role;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.NotFoundException;
import com.example.coursemanagement.model.projection.UserPublic;
import com.example.coursemanagement.model.request.CreateUserRequest;
import com.example.coursemanagement.model.request.UpdateUserRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.repository.RoleRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.UserService;
import com.example.coursemanagement.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileServiceImpl fileServiceImpl;

    // Lấy danh sách user có phân trang
    @Override
    public PageUtil<UserPublic> getAll(int page, int size) {
        Page<User> pageInfo = userRepository.findAll(PageRequest.of(page - 1, size));

        return new PageUtil<>(
                pageInfo.getContent().stream()
                        .map(user -> UserPublic.of(user)).toList(),
                pageInfo.getPageable().getPageNumber() + 1,
                pageInfo.getPageable().getPageSize(),
                IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList(),
                pageInfo.getTotalElements(),
                pageInfo.isFirst(),
                pageInfo.isLast()
        );
    }

    // Lấy danh sách user thep role
    @Override
    public List<UserPublic> getUserByRole(String roleName) {
        return userRepository.findByRoles_NameIgnoreCase(roleName)
                .stream()
                .map(user -> UserPublic.of(user))
                .toList();
    }

    // Lấy chi tiết user theo id
    @Override
    public UserPublic getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found user with id = " + id);
        });

        return UserPublic.of(user);
    }

    // Tạo user
    @Override
    public UserPublic createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email = " + request.getEmail() + " is existed");
        }

        // List Role
        List<Role> roleList = roleRepository.findByIdIn(request.getRoleIds());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleList)
                .build();

        userRepository.save(user);
        return UserPublic.of(user);
    }

    // Cập nhật thông tin user
    @Override
    public UserPublic updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found user with id = " + id);
        });

        // List Role
        List<Role> roleList = roleRepository.findByIdIn(request.getRoleIds());

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRoles(roleList);

        userRepository.save(user);
        return UserPublic.of(user);
    }

    // Thay đổi avatar
    @Override
    public FileResponse uploadAvatar(Integer id, MultipartFile file) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found user with id = " + id);
        });

        // Upload file
        FileResponse fileResponse = fileServiceImpl.uploadFile(file);

        user.setAvatar(fileResponse.getUrl());
        userRepository.save(user);

        return fileResponse;
    }
}
