package net.guzari.search.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.guzari.search.rest.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);

        try {
            String jwtToken = jwtUtils.validateHeader(authHeader);
            Claims claims = jwtUtils.validateToken(jwtToken);
            String email = claims.getSubject();
            User user = new User(email, "", new ArrayList<>());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                    null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (CustomException ex) {
            logger.error("Error occurred: {}", ex.getMessage(), ex);
            response.getWriter().print(new ObjectMapper().writeValueAsString(ex));
            response.setContentType("application/json");
            response.setStatus(ex.getCode());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
