package pl.termosteam.kinex.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@RequiredArgsConstructor
public class JwtToken {

    private final ApplicationProperties applicationProperties;

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
        return Jwts.parser().setSigningKey(applicationProperties.getJwtConfig().getSECRET()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    Date addDurationToDate(Date started, Duration duration) {
        LocalDateTime localDateTime = new java.sql.Timestamp(started.getTime()).toLocalDateTime();
        localDateTime = localDateTime.plus(duration);
        return java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String generateAuthenticationToken(UserDetails userDetails, Date started) {
        Date expired = addDurationToDate(started,
                applicationProperties.getJwtConfig().getJWT_TOKEN_VALIDITY());
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), started, expired);
    }

    public String generateActivationToken(String uuid, Date started) {
        Date expired = addDurationToDate(started,
                applicationProperties.getJwtConfig().getJWT_TOKEN_VALIDITY_ACTIVATION_TIME());
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, uuid, started, expired);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Date started, Date expired) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(started)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtConfig().getSECRET())
                .compact();
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