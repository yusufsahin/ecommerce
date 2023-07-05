package net.provera.categoryserv.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.provera.categoryserv.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        final String authHeader = req.getHeader(Constant.HEADER_STRING);
        String jwt;
        String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(req, res);
            return;
        }
        jwt = authHeader.substring(7);

        try{
            username = jwtTokenUtil.getUsernameFromToken(jwt);
        }
        catch (JwtException ex){
            filterChain.doFilter(req, res);
            return;
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(jwtTokenUtil.validateToken(jwt)){
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                for (String rolename : jwtTokenUtil.getRoleNamesFromToken(jwt)) {
                    authorities.add(new SimpleGrantedAuthority(rolename));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(req)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(req, res);
    }

}
