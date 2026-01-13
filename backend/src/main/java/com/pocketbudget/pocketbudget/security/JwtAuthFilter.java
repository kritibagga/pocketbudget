package com.pocketbudget.pocketbudget.security;

import java.io.IOException;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthFilter implements Filter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException{
                        HttpServletRequest http =(HttpServletRequest) request;
                        String authHeader= http.getHeader("Authorization");

                        if(authHeader!=null && authHeader.startsWith("Bearer")){
                            String token =authHeader.substring(7);

                            if(jwtService.isTokenValid(token)){
                                Long userId=jwtService.getUserIdFromToken(token);

                                UserDetails userDetails=userDetailsService.loadUserByUsername(userId.toString());

                                UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                                SecurityContextHolder.getContext().setAuthentication(authentication);

                            }
                        }
                        chain.doFilter(request,response);

                    }




}
