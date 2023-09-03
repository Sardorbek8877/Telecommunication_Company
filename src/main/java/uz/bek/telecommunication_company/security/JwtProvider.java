package uz.bek.telecommunication_company.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.bek.telecommunication_company.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private static final long expireTime = 1000 * 60 * 60 * 24;
    private static final String secretKey = "SecretWord";

    public String generateToken(String username, Set<Role> role){
        Date expireTime = new Date(System.currentTimeMillis() + JwtProvider.expireTime);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .claim("roles", role)
                .signWith(SignatureAlgorithm.ES512, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token){
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (Exception e){
            return null;
        }
    }
}
