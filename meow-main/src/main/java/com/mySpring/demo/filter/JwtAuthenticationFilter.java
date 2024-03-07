package com.mySpring.demo.filter;

import com.mySpring.demo.models.user.pojos.User;
import com.mySpring.demo.utils.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            User user = extractUser(token);
            if (user != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }

        return null;
    }

    private User extractUser(String token) {
        try {
            Claims claims = JwtTokenProvider.parsePayload(token);
            String username = claims.get("username", String.class);
            String uuid = claims.getId();

            User user = new User();
            user.setUsername(username);
            user.setUUID(uuid);

            return user;
        } catch (Exception e) {
            // 处理解析异常
            return null;
        }
    }
}
