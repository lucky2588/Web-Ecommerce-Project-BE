package com.total.webecommerce.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomFilter extends OncePerRequestFilter{
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        // Todo : check xem header co "Authozited " or "Bear" hay k?
        if(authHeader == null || !authHeader.contains("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        // TODO : lay token tu header
        String jwtToken = authHeader.substring(7);

        // TODO : lay ra thong tin Username Email
        String UserEmail = jwtUtils.extractUsername(jwtToken);

        // TODO : ktra Email va tao doi tuong xac thuc
        if(UserEmail != null){

            UserDetails userDetails = customUserDetailService.loadUserByUsername(UserEmail);
            userDetails.getAuthorities().forEach(System.out::println);
            if(jwtUtils.isTokenValid(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
