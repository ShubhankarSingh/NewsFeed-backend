package com.newsfeed.auth.auth_service.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

        private final JWTConfigProperties configProperties;

        private JWTService(JWTConfigProperties configProperties){
            this.configProperties = configProperties;
        }

        private Key getSigningKey(){
            return Keys.hmacShaKeyFor(configProperties.getSecret().getBytes());
        }

        public String generateToken(String username){
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + configProperties.getExpiration()))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractUsername(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .getSubject();
        }

        public boolean isTokenValid(String token, String userEmail){
            return extractUsername(token).equals(userEmail) && !isTokenExpired(token);
        }

        public boolean isTokenExpired(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        }
}
