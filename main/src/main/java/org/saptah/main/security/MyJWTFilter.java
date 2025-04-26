package org.saptah.main.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.saptah.main.user.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyJWTFilter extends OncePerRequestFilter {

    private final JWTService jwtservice;

    private final MyUserDetailService myuserdetailservice;

    private final ObjectMapper objectmapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try{
            jwtservice.checkMalFormedJwt(token);
            String userName = jwtservice.extractUserName(token);
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                // get UserDetails
                UserDetails user = myuserdetailservice.loadUserByUsername(userName);
                if(jwtservice.validateToken(token, user)){
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                filterChain.doFilter(request,response);
            }
        }
        catch(JwtException e){
            handleJWTError(response, e);
            return; //stop further processing, this does not give error on console
        }
    }

    private void handleJWTError(HttpServletResponse response, JwtException e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status",HttpStatus.UNAUTHORIZED.value());
        errorMap.put("message","Invalid jwt format");
        errorMap.put("timestamp", LocalDateTime.now());
        objectmapper.writeValue(response.getWriter(),errorMap);

        // no need for global controller advice, as SecurityFilterChain exceptions lies outside the
        // scope of MVC.
    }
}
