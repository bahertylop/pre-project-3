package org.backend.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.backend.model.Role;
import org.backend.model.User;
import org.dto.response.JwtTokensResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${token.access.key}")
    private String accessTokenKey;

    @Value("${token.refresh.key}")
    private String refreshTokenKey;

    public String extractUserNameAccessToken(String token) {
        return extractUserEmail(token, accessTokenKey);
    }

    public String extractUserNameRefreshToken(String token) {
        return extractUserEmail(token, refreshTokenKey);
    }

    private String extractUserEmail(String token, String key) {
        return extractClaim(token, Claims::getSubject, key);
    }

    public JwtTokensResponse generateTokens(UserDetails userDetails) {
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return JwtTokensResponse.builder()
                .access(accessToken)
                .refresh(refreshToken)
                .build();
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        User user = (User) userDetails;
        claims.put("id", user.getId());
        claims.put("chat_id", user.getChatId());
        claims.put("roles", user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(accessTokenKey), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return  Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 2))
                .signWith(getSigningKey(refreshTokenKey), SignatureAlgorithm.HS256).compact();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, accessTokenKey);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, refreshTokenKey);
    }

    private boolean isTokenValid(String token, UserDetails userDetails, String key) {
        final String userEmail = extractUserEmail(token, key);
        return (userDetails.getUsername().equals(userEmail) && !isTokenExpired(token, key));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers, String key) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token, String key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token, String key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    private Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey(key)).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
