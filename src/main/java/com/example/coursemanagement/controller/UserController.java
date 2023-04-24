package com.example.coursemanagement.controller;

import com.example.coursemanagement.entity.Role;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.model.projection.RolePublic;
import com.example.coursemanagement.model.projection.UserPublic;
import com.example.coursemanagement.model.request.CreateUserRequest;
import com.example.coursemanagement.model.request.UpdateUserRequest;
import com.example.coursemanagement.model.response.FileResponse;
import com.example.coursemanagement.service.RoleService;
import com.example.coursemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    // Danh sách View
    @GetMapping("admin/users")
    public String getListUserPage(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        model.addAttribute("data", userService.getAll(page, size));

        return "admin/user/user-list";
    }

    @GetMapping("admin/users/create")
    public String getCreateUserPage(Model model) {
        List<RolePublic> roleList = roleService.getAll();
        model.addAttribute("roleList", roleList);

        return "admin/user/user-create";
    }

    @GetMapping("admin/users/{id}")
    public String getDetailUserPage(@PathVariable Integer id, Model model) {
        List<RolePublic> roleList = roleService.getAll();
        UserPublic user = userService.getUserById(id);

        model.addAttribute("roleList", roleList);
        model.addAttribute("user", user);

        return "admin/user/user-edit";
    }

    // Danh sách API
    // Tạo user mới
    @PostMapping("api/admin/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserPublic user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Cập nhật thông tin user
    @PutMapping("api/admin/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateUserRequest request) {
        UserPublic user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    // Upload avatar
    @PostMapping("api/admin/users/{id}/upload-avatar")
    public ResponseEntity<?> uploadAvatar(
            @ModelAttribute("file") MultipartFile file,
            @PathVariable Integer id) {
        FileResponse fileResponse = userService.uploadAvatar(id, file);
        return ResponseEntity.ok(fileResponse);
    }
}
