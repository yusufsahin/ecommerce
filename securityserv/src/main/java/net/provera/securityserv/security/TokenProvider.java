package net.provera.securityserv.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import net.provera.securityserv.dao.RoleRepository;
import net.provera.securityserv.dao.UserRepository;
import net.provera.securityserv.dao.entity.Role;
import net.provera.securityserv.dao.entity.User;
import net.provera.securityserv.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements Serializable {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Constant.SIGNING_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
       /* final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        */

        SecretKey key = Keys.hmacShaKeyFor(Constant.SIGNING_KEY.getBytes());


        final List<Role> roles = authentication.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> roleRepository.findByName(authority.getAuthority())).collect(Collectors.toList());

        final List<String> scopes = roles.stream().map(role -> {
            return role.getName();
        }).collect(Collectors.toList());

        final List<JWTAuthorityEntity> authorities = roles.stream().map(role -> new JWTAuthorityEntity(role.getName(),
                        role.getPrivileges().stream().map(privilege -> privilege.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(Constant.AUTHORITIES_KEY, scopes)
                .claim("authorities",authorities)
                //.claim("user", userRepository.findByUsername(authentication.getName()))
                .setHeaderParam("typ","JWT")
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constant.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .compact();
    }

    public String generateTokenByUser(User user){
        SecretKey key = Keys.hmacShaKeyFor(Constant.SIGNING_KEY.getBytes());

        final Collection<Role> roleEntities = user.getRoles();
        //Database gets roles with their copy so we should take one of them
        final Collection<Role> roles = new ArrayList<>();
        for (Role role : roleEntities){
            boolean isCopied = false;
            for(Role role1 : roles){
                if (role.getName().equals(role1.getName())){
                    isCopied = true;
                }
            }
            if(!isCopied)
                roles.add(role);
        }

        final List<String> scopes = roles.stream().map(role -> {
            return role.getName();
        }).collect(Collectors.toList());

        final List<JWTAuthorityEntity> authorities = roles.stream().map(role -> new JWTAuthorityEntity(role.getName(),
                        role.getPrivileges().stream().map(privilege -> privilege.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(Constant.AUTHORITIES_KEY, scopes)
                .claim("authorities",authorities)
                //.claim("user", userRepository.findByUsername(authentication.getName()))
                .setHeaderParam("typ","JWT")
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constant.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(Constant.SIGNING_KEY.getBytes());

        final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final List<String> roles = (List<String>) claims.get(Constant.AUTHORITIES_KEY);
        final List<LinkedHashMap<String,Object>> authorityEntities = (List<LinkedHashMap<String,Object>>) claims.get("authorities");
        final List<String> addedAuthorities = new ArrayList<>();
        final Collection<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        /*
        roles.stream().forEach(role -> roleRepository.findByName(role).getPrivileges().stream()
                            .filter(privilege -> !addedAuthorities.contains(privilege.getName())).forEach(privilege -> {
                                authorities.add(new SimpleGrantedAuthority(privilege.getName()));
                                addedAuthorities.add(privilege.getName());
                            }));

         */

        for (LinkedHashMap<String,Object> authority: authorityEntities){
            ((List<String>)authority.get("privileges")).stream()
                    .filter(privilege -> !addedAuthorities.contains(privilege)).forEach(privilege -> {
                        authorities.add(new SimpleGrantedAuthority(privilege));
                        addedAuthorities.add(privilege);
                    });
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }



}

