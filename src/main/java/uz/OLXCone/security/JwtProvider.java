package uz.OLXCone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.OLXCone.exception.ConflictException;

import java.util.Date;


@Component
@Slf4j
public class JwtProvider {
    @Value("${command.expire-time}")
    Long expiration;
    @Value("${command.jwt-key}")
    String secretKey;

    public String generateJWT(String subject) {
        String compact = "";
        try {
            compact = Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
        } catch (Exception e) {
           log.warn("Defined exception on generateJWT Method",e);
           throw new ConflictException("some error");
        }
        return compact;
    }

    public String getSubjectFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
