package pl.termosteam.kinex.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtRequestFilter: class setups the Requests Filter in configuration.
 * Security is based on JWT token
 * extends OncePerRequestFilter
 *
 * @author Dmytyro Lumelskyj
 * @version 1.0
 * @since 2020-01-01
 */
@Component
public class JwtToken {

    @Value("${jwt.token.validity.time:180000}")
    public static long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateActivationToken(String uuid) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, uuid);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {


        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addDays(new Date(System.currentTimeMillis()), 5))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getSubjectFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateActivationToken(String token, String userActivationUUID) {
        final String activationUUID = getSubjectFromToken(token);
        return (activationUUID.equals(userActivationUUID) && !isTokenExpired(token));
    }
}