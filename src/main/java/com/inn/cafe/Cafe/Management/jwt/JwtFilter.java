package com.inn.cafe.Cafe.Management.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    private Claims claims;
    private String username = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Public endpoints that don't need authentication
        if (request.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // 1️⃣ Authorization header missing or invalid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            unauthorized(response, "Please authenticate the user. Token missing.");
            return;
        }

        String jwtToken = authHeader.substring(7).trim();

        // 2️⃣ Token is empty
        if (jwtToken.isEmpty()) {
            unauthorized(response, "Please authenticate the user. Token is empty.");
            return;
        }

        try {
            username = jwtUtil.extractUserName(jwtToken);
            claims = jwtUtil.extractAllClaims(jwtToken);
        } catch (Exception e) {
            unauthorized(response, "Invalid or expired token. Please login again.");
            return;
        }

        // 3️⃣ User authentication setup
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customerUsersDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                // Extract role directly from the validated token string
                String roleFromToken = (String) claims.get("role"); // "ADMIN" or "USER"

                // Prepend "ROLE_" to satisfy Spring Security's internal hasRole() requirements
                String formattedRole = "ROLE_" + roleFromToken.toUpperCase();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(formattedRole);

                // Create the token using the role extracted directly from the token payload
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, Collections.singletonList(authority));

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Helper method for unauthorized response
    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    public Boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public Boolean isUser() {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        return username;
    }
}