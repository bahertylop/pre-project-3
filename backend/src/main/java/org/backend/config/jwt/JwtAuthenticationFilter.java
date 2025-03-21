package org.backend.config.jwt;


import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.info("request not have auth header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            String userEmail = jwtService.extractUserNameAccessToken(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("request by user with email: {}", userEmail);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isAccessTokenValid(token, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                    log.info("user with email: {} authorized", userDetails.getUsername());
                }
            }
        } catch (JwtException e) {
            log.error("not valid jwt token for request", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Невалидный jwt токен");
            return;
        } catch (UsernameNotFoundException e) {
            log.error("user not found", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Пользователь не найден");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
