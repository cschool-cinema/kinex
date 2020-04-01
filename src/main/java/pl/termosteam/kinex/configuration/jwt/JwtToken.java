package pl.termosteam.kinex.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.termosteam.kinex.configuration.properties.JwtConfiguration;

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

    private final JwtConfiguration jwtConfiguration;

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
        return Jwts.parser().setSigningKey(jwtConfiguration.getSECRET()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateAuthenticationToken(UserDetails userDetails, Date started) {
        Date expired = DateUtils.addMinutes(started, jwtConfiguration.getJWT_TOKEN_VALIDITY_IN_MIN());
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), started, expired);
    }

    public String generateActivationToken(String uuid, Date started) {
        Date expired = DateUtils.addMinutes(started, jwtConfiguration.getJWT_TOKEN_VALIDITY_ACTIVATION_TIME_IN_MIN());
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, uuid, started, expired);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Date started, Date expired) {
        //Date startedAt = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(started)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSECRET())
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