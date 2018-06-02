package cn.ghy.liumall.security;

import cn.ghy.liumall.model.WeChatLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateWechatToken(WeChatLogin weChatLogin, int expiration) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return "Bearer "+
                Jwts.builder()
                .setSubject(weChatLogin.getOpenId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getOpenIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token.replace("Bearer ",""))
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        String token = authToken;
        if (authToken.indexOf("Bearer ") > -1){
            token = authToken.replace("Bearer ","");
        }
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
