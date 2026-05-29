package com.example.resumebuilder.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.resumebuilder.document.User;
import com.example.resumebuilder.respository.UserRepository;
import com.example.resumebuilder.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String userId = null;

        //////////////////////////////////////////////////////
        // EXTRACT TOKEN
        //////////////////////////////////////////////////////

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {

                userId = jwtUtil.getUserIdFromToken(token);

            } catch (Exception e) {

                log.error("Invalid token", e);
            }
        }

        //////////////////////////////////////////////////////
        // AUTHENTICATE USER
        //////////////////////////////////////////////////////

        if (userId != null
                && SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            try {

                boolean isValid =
                        jwtUtil.validateToken(token);

                boolean isExpired =
                        jwtUtil.isTokenExpired(token);

                if (isValid && !isExpired) {

                    User user =
                            userRepository.findById(userId)
                                    .orElseThrow(() ->
                                            new UsernameNotFoundException(
                                                    "User not found"
                                            )
                                    );

                    //////////////////////////////////////////////////////
                    // STORE USER ID AS PRINCIPAL
                    //////////////////////////////////////////////////////

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user.getId(),
                                    null,
                                    new ArrayList<>()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }

            } catch (Exception e) {

                log.error("Token validation failed", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}