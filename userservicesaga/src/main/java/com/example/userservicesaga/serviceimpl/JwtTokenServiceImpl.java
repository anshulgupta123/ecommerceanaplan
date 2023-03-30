package com.example.userservicesaga.serviceimpl;

import com.example.userservicesaga.entity.User;
import com.example.userservicesaga.exception.InvalidCredentialsException;
import com.example.userservicesaga.exception.UserException;
import com.example.userservicesaga.service.JwtTokenService;
import com.example.userservicesaga.utility.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    @Autowired
    Environment environment;


    @Override
    public Map<String, String> generateToken(User user) {
        try {
            log.info("Inside generateToken of JwtTokenServiceImpl");
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.PASSWORD, user.getPassword());
            String jwtToken = Jwts.builder().setIssuer(Constants.USER_APPLICATION)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date()).addClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_TOKEN_VALIDITY * 1000))
                    .signWith(SignatureAlgorithm.HS256, Constants.MYSECRET)
                    .compact();
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TOKEN, jwtToken);
            map.put(Constants.EMAIL, user.getEmail());
            map.put(Constants.Message, "Authentication Successful");
            return map;
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof InvalidCredentialsException) {
                errorMessage = ((InvalidCredentialsException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in generateToken of JwtTokenServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(environment.getProperty(Constants.MYSECRET)).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getFirstName()) && !isTokenExpired(token));
    }


}

