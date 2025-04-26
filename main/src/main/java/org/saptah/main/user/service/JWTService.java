package org.saptah.main.user.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.saptah.main.user.entity.AdminUser;
import org.saptah.main.user.entity.BaseUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @PostConstruct
    public void init() {
        //System.out.println("Secret key: " + secretKey);
        //System.out.println("bytes: "+Decoders.BASE64.decode(secretKey).length);
    }

    @Value("${application.security.jwt.secret}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    public String generateJWT(BaseUser user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", user.getAuthorities());
        return createToken(claims, user.getUsername());
    }

    private String createToken(Map<String,Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (keyBytes.length < 32) { // HS256 requires min 256 bits (32 bytes)
            throw new IllegalArgumentException("Secret key must be at least 256 bits (32 bytes)");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userdetails){
        final String userName = extractUserName(token);
        return (userName.equals(userdetails.getUsername()) && !isTokenExpired(token));
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public void checkMalFormedJwt(String token) {
        if(token == null || token.trim().isEmpty()){
            throw new JwtException("Empty jwt string");
        }
        String[] parts = token.split("\\.");
        if(parts.length != 3){
            throw new JwtException("Invalid jwt format");
        }
        try{
             Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch(MalformedJwtException e){
            throw new JwtException("Invalid jwt structure",e);
        }
        catch(UnsupportedJwtException e){
            throw new JwtException("Unsupported jwt",e);
        }
        catch(ExpiredJwtException e){
            throw new JwtException("Expired jwt", e);
        }
        catch(JwtException e){
            throw new JwtException("Jwt validation failed",e);
        }
    }
}
