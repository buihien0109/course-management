package com.example.coursemanagement.service;

import com.example.coursemanagement.model.projection.UserPublic;
import com.example.coursemanagement.model.request.CreateUserRequest;
import com.example.coursemanagement.model.request.UpdateUserRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.util.PageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    PageUtil<UserPublic> getAll(int page, int size);

    List<UserPublic> getUserByRole(String roleName);

    UserPublic getUserById(Integer id);

    UserPublic createUser(CreateUserRequest request);

    UserPublic updateUser(Integer id, UpdateUserRequest request);

    FileResponse uploadAvatar(Integer id, MultipartFile file);
}
