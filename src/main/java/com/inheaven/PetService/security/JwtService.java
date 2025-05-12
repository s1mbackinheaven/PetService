package com.inheaven.PetService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inheaven.PetService.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Collections;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration:86400000}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        extraClaims.put("created", new Date(System.currentTimeMillis()));

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = null;
        try {
            if (isBase64(secretKey)) {
                keyBytes = Decoders.BASE64.decode(secretKey);
            } else {
                keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xử lý secret key: {}", e.getMessage());
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isBase64(String str) {
        try {
            Decoders.BASE64.decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Map<String, Object> validateToken(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            result.put("valid", true);
        } catch (SignatureException e) {
            result.put("valid", false);
            result.put("errorType", "SIGNATURE_MISMATCH");
            result.put("error", e.getMessage());
        } catch (ExpiredJwtException e) {
            result.put("valid", false);
            result.put("errorType", "TOKEN_EXPIRED");
            result.put("error", e.getMessage());
        } catch (MalformedJwtException e) {
            result.put("valid", false);
            result.put("errorType", "MALFORMED_TOKEN");
            result.put("error", e.getMessage());
        } catch (UnsupportedJwtException e) {
            result.put("valid", false);
            result.put("errorType", "UNSUPPORTED_TOKEN");
            result.put("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            result.put("valid", false);
            result.put("errorType", "EMPTY_PAYLOAD");
            result.put("error", e.getMessage());
        } catch (Exception e) {
            result.put("valid", false);
            result.put("errorType", "UNKNOWN_ERROR");
            result.put("error", e.getMessage());
        }
        return result;
    }

    public Map<String, Object> validateTokenWithoutSignature(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                result.put("valid", false);
                result.put("errorType", "MALFORMED_TOKEN");
                result.put("error", "Token không đúng định dạng");
                return result;
            }

            String payload = new String(Decoders.BASE64.decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode claims = mapper.readTree(payload);

            if (claims.has("exp")) {
                long exp = claims.get("exp").asLong();
                if (exp < System.currentTimeMillis() / 1000) {
                    result.put("valid", false);
                    result.put("errorType", "TOKEN_EXPIRED");
                    result.put("error", "Token đã hết hạn");
                    return result;
                }
            }

            if (claims.has("sub")) {
                String username = claims.get("sub").asText();
                result.put("username", username);
            }

            result.put("valid", true);
        } catch (Exception e) {
            result.put("valid", false);
            result.put("errorType", "UNKNOWN_ERROR");
            result.put("error", e.getMessage());
        }
        return result;
    }

    public Map<String, Object> debugToken(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                result.put("error", "Token không đúng định dạng");
                return result;
            }

            String header = new String(Decoders.BASE64.decode(parts[0]));
            result.put("header", header);

            String payload = new String(Decoders.BASE64.decode(parts[1]));
            result.put("payload", payload);

            result.put("signature", parts[2]);

            String calculatedSignature = calculateSignature(parts[0] + "." + parts[1]);
            result.put("calculatedSignature", calculatedSignature);

            result.put("secretKeyLength", secretKey.length());
            result.put("isBase64", isBase64(secretKey));

            return result;
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return result;
        }
    }

    private String calculateSignature(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(getSigningKey());
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Encoders.BASE64URL.encode(bytes);
        } catch (Exception e) {
            return "Error calculating signature: " + e.getMessage();
        }
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole());
        claims.put("created", user.getCreated());
        claims.put("status", user.getStatus());
        claims.put("fullname", user.getFullname());
        claims.put("gender", user.getGender());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhone());
        claims.put("address", user.getAddress());
        claims.put("avatar", user.getAvatar());
        claims.put("experience", user.getExperience());
        return buildToken(claims, new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))), jwtExpiration);
    }
}