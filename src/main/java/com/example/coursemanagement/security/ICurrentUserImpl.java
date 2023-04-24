package com.example.coursemanagement.security;

import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.NotFoundException;
import com.example.coursemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ICurrentUserImpl implements ICurrentUser {
    private final UserRepository userRepository;

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Not found user");
                    });
        }
        return null;
    }
}
