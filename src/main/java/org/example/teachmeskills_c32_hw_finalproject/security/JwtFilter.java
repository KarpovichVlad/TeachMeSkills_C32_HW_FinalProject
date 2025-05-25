package org.example.teachmeskills_c32_hw_finalproject.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter implements Filter {
    private JwtUtil jwtUtil;
    private CustomUserDetailService customUserDetailService;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailService customUserDetailService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<String> token = jwtUtil.getTokenFromRequest(request);
        if (token.isPresent() && jwtUtil.validateToken(token.get())) {
            Optional<String> login = jwtUtil.getLoginFromToken(token.get());
            if (login.isPresent()) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(login.get());
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(upat);
                log.info("Authenticated user: {}", userDetails.getUsername());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
