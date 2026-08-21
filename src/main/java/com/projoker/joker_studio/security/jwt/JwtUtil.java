package com.projoker.joker_studio.security.jwt;

import com.projoker.joker_studio.security.user_details.StudioUserDetails;
import com.projoker.joker_studio.security.user_details.StudioUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    private final long expiry=1000*60*60;

    public String generateToken(Authentication authentication){
        StudioUserDetails userDetails= (StudioUserDetails) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                //here i am sending the user id + roles with in of key
                .claim("id",userDetails.getId())
                .claim("role",roles)
                .setIssuedAt(new Date())
                .signWith(key(), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis()+expiry))
                .compact();
    }

    public Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getContext(String token){
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token){
        return getContext(token).getSubject();
    }

    public boolean validateToken(String username, String token, UserDetails userDetails){
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return getContext(token).getExpiration().before(new Date());
    }
}
