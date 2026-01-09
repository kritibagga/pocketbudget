package com.pocketbudget.pocketbudget.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.pocketbudget.pocketbudget.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props){
        this.props=props;
        this.key= Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user){
        Instant now =Instant.now();
        Instant exp=now.plus(props.getAccessTokenMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(user.getId().toString())
                .claim("role", user.getRole().name())
                .claim("householdId", user.getHousehold().getId())
                .issuedAt(new Date())
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromToken(String token){
       Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
