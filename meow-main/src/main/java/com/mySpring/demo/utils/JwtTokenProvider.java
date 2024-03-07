package com.mySpring.demo.utils;

import com.mySpring.demo.models.user.pojos.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    public static final int EXPIRATION_TIME = 60 * 60 * 1000;

    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;

    private final static String SECRET = "meowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeow";

    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final static String JWT_ISS = "meow";

    private final static String SUBJECT = "meow";

    public static String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                // 自定义负载信息
                .claim("userid", user.getId())
                .claim("username", user.getUsername())
                // 令牌ID
                .id(user.getUUID())
                // 过期日期
                .expiration(expirationDate)
                // 签发时间
                .issuedAt(new Date())
                // 主题
                .subject(SUBJECT)
                // 签发者
                .issuer(JWT_ISS)
                // 签名
                .signWith(KEY, ALGORITHM)
                .compact();
    }

    public static User getUserFromToken(String token) {
        Claims claims = parsePayload(token);
        User user = new User();
        user.setId(claims.get("userid", Long.class));
        user.setUsername(claims.get("username", String.class));
        user.setUUID(claims.getId());
        return user;
    }

    public static Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }

    public static JwsHeader parseHeader(String token) {
        return parseClaim(token).getHeader();
    }

    public static Claims parsePayload(String token) {
        return parseClaim(token).getPayload();
    }
}
