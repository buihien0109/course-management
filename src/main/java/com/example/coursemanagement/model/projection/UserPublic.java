package com.example.coursemanagement.model.projection;

import com.example.coursemanagement.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface UserPublic {
    Integer getId();

    String getName();

    String getEmail();

    String getPhone();

    String getAvatar();

    List<RolePublic> getRoles();

    @RequiredArgsConstructor
    class UserPublicImpl implements UserPublic {
        @JsonIgnore
        private final User user;

        @Override
        public Integer getId() {
            return this.user.getId();
        }

        @Override
        public String getName() {
            return this.user.getName();
        }

        @Override
        public String getEmail() {
            return this.user.getEmail();
        }

        @Override
        public String getPhone() {
            return this.user.getPhone();
        }

        @Override
        public String getAvatar() {
            return this.user.getAvatar();
        }

        @Override
        public List<RolePublic> getRoles() {
            return this.user.getRoles()
                    .stream()
                    .map(role -> RolePublic.of(role))
                    .toList();
        }
    }

    static UserPublic of(User user) {
        return new UserPublicImpl(user);
    }
}
