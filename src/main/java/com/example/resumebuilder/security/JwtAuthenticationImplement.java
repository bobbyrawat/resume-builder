package com.example.resumebuilder.security;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import java.util.Map;
import java.util.HashMap;

@Component
public class JwtAuthenticationImplement implements   AuthenticationEntryPoint{
    

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException{


        response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Not authorized,no token");

        ObjectMapper mapper = new ObjectMapper();
       mapper.writeValue(response.getOutputStream(), errorResponse);

    }
}
