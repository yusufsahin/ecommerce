package net.provera.categoryserv.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.provera.categoryserv.util.Constant;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class TokenProvider implements Serializable {

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String[] getRoleNamesFromToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(Constant.SIGNING_KEY.getBytes());

        final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final List<Map<String, Object>> roles = (List<Map<String, Object>>) claims.get("authorities");

        List<String> userRoles = new ArrayList<>();
        roles.stream().forEach((map) -> {
            userRoles.add((String) map.get("role"));

            ((ArrayList<String>) map.get("privileges")).stream().forEach((privilege) -> {
                userRoles.add(privilege);
            });
        });

        return userRoles.toArray(new String[0]);
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

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return (
                username != null
                        && !isTokenExpired(token));
    }

}

