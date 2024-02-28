package org.work.personnelinfo.config.Security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilterUtil extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilterUtil.class);

    public JwtFilterUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                logger.warn("Request to parse expired JWT : {} failed : {}", token, e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.warn("Request to parse unsupported JWT : {} failed : {}", token, e.getMessage());
            } catch (MalformedJwtException e) {
                logger.warn("Request to parse invalid JWT : {} failed : {}", token, e.getMessage());
            } catch (SignatureException e) {
                logger.warn("Request to parse JWT with invalid signature : {} failed : {}", token, e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.warn("Request to parse empty or null JWT : {} failed : {}", token, e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}