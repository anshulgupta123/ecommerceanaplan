package com.example.userservicesaga.service;

import com.example.userservicesaga.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public interface JwtTokenService {
    Map<String, String> generateToken(User user);

    public String getUsernameFromToken(String token);

    public Date getExpirationDateFromToken(String token);

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    public Claims getAllClaimsFromToken(String token);

    public Boolean isTokenExpired(String token);

    public Boolean validateToken(String token, User user);


}