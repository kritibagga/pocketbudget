package com.pocketbudget.pocketbudget.security;

import java.io.IOException;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

                        // If no token, just continue. Some endpoints (auth) are public.
                        if(authHeader==null || !authHeader.startsWith("Bearer ")){
                            chain.doFilter(request, response);
                            return;
                        }
                        //extract token after "Bearer "
                        String token =authHeader.substring(7);


                        //validate token signature and expiry
                        if(!jwtService.isTokenValid(token)){
                            chain.doFilter(request, response);
                            return;
                        }

                        //Read Claims
                        Claims claims=jwtService.getClaims(token);

                        Long userId=Long.parseLong(claims.getSubject());
                        Long householdId=((Number) claims.get("householdId")).longValue();
                        String role=(String) claims.get("role");

                        //Load Authorities from DB
                        UserDetails userDetails=userDetailsService.loadUserByUsername(userId.toString());

                        //put our custom principal in the security context
                        CurrentUser principal=new CurrentUser(userId, householdId, role);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(principal,null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);




                    }




}
