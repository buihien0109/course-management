package com.example.coursemanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomFilter extends OncePerRequestFilter {
    @Qualifier("userDetailsService")
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Lấy userEmail từ trong session
        String userEmail = (String) request.getSession().getAttribute("MY_SESSION");
        log.info("userEmail : {}", userEmail);

        // Kiểm tra userEmail
        if(userEmail == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy thông tin user
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        log.info("userDetails : {}", userDetails);
        log.info("roles : {}", userDetails.getAuthorities());

        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Xác thực thành công, lưu object Authentication vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
