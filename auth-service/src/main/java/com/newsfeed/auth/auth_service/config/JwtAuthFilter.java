package com.newsfeed.auth.auth_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = getJwtFromRequest(request);

            if(jwt!=null){
                String username = jwtService.extractUsername(jwt);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() != null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if(jwtService.isTokenValid(jwt, userDetails.getUsername())){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }catch (Exception ex) {
            logger.error("Cannot set user authentication: {}", ex);
        }

        filterChain.doFilter(request, response);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null || authHeader.startsWith("Bearer ")){
            return authHeader.substring(7); //Remove "Bearer "
        }
        return null;
    }
}
