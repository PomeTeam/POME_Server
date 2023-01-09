package com.example.pomeserver.global.util.jwtToken;

import com.example.pomeserver.global.exception.excute.TokenIsNotValidException;
import io.jsonwebtoken.*;
import io.netty.handler.codec.compression.CompressionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenUtils {
    public static final String USER_ID = "userId";
    public static final String NICKNAME = "nickname";
    public static final String ONE_BLOCK = " ";

    public enum TYPE{
        REFRESH,
        ACCESS
    }

    public static String secretKey = "";
    public static String tokenType = "";
    public static String accessName = "";
    public static String refreshName = "";
    public static String accessExTime = "";
    public static String refreshExTime = "";

//    @Value("${jwt.secret}")
//    public void setSecretKey(String value){
//        secretKey = value;
//    }
//
//    @Value("${jwt.token_type}")
//    public void setTokenType(String value){
//        tokenType = value;
//    }
//
//    @Value("${jwt.access_name}")
//    public void setAccessName(String value){
//        accessName = value;
//    }
//
//    @Value("${jwt.refresh_name}")
//    public void setRefreshName(String value){
//        refreshName = value;
//    }
//
//    @Value("${jwt.access_expired_time}")
//    public void setAccessExpiredTime(String value){
//        accessExTime = value;
//    }
//
//    @Value("${jwt.refresh_expired_time}")
//    public void setRefreshExpireTime(String value){
//        refreshExTime = value;
//    }

    public String createAccessToken(Long id, String email, String nickname, String role){
        Claims claims = Jwts.claims()
                .setSubject(accessName)
                .setIssuedAt(new Date());
        claims.put(USER_ID, id);
        claims.put(NICKNAME, nickname);
        Date ext = new Date();
        ext.setTime(ext.getTime() + Integer.parseInt(Objects.requireNonNull(accessExTime)));
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return tokenType+ONE_BLOCK+accessToken;
    }

    public String createRefreshToken(Long id, String email, String nickname, String role){
        Claims claims = Jwts.claims()
                .setSubject(refreshName)
                .setIssuedAt(new Date());
        claims.put(USER_ID, id);
        claims.put(NICKNAME, nickname);
        Date ext = new Date();
        ext.setTime(ext.getTime() + Integer.parseInt(Objects.requireNonNull(refreshExTime)));
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return tokenType+ONE_BLOCK+accessToken;
    }

    public boolean isValidToken(String justToken) {
        if(justToken!=null && justToken.split(ONE_BLOCK).length == 2)
            justToken = justToken.split(ONE_BLOCK)[1];
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(justToken).getBody();
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Tamperd");
            return true;
        } catch (MalformedJwtException exception) {
            log.error("Token MalformedJwtException");
            return false;
        } catch (ClaimJwtException exception) {
            log.error("Token ClaimJwtException");
            return false;
        } catch (UnsupportedJwtException exception) {
            log.error("Token UnsupportedJwtException");
            return false;
        } catch (CompressionException exception) {
            log.error("Token CompressionException");
            return false;
        } catch (RequiredTypeException exception) {
            log.error("Token RequiredTypeException");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        } catch (Exception exception){
            log.error("Undefined ERROR");
            return false;
        }
    }

    private Claims getJwtBodyFromJustToken(String justToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(justToken)
                    .getBody();
        } catch(UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | ExpiredJwtException e) {
            throw new TokenIsNotValidException(); //TODO
        }
    }

    public boolean isTokenExpired(String justToken){
        if(justToken!=null && justToken.split(ONE_BLOCK).length == 2)
            justToken = justToken.split(ONE_BLOCK)[1];
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(justToken).getBody();
        } catch(ExpiredJwtException e) {
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Long getUserIdFromFullToken(String fullToken){
        return Long.valueOf((Integer) getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get(USER_ID));
    }

    public String getNicknameFromFullToken(String fullToken){
        return (String) getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get(NICKNAME);
    }

    public Long getUserIdFromJustToken(String justToken){
        return Long.valueOf((Integer) getJwtBodyFromJustToken(justToken).get(USER_ID));
    }

    public String getNicknameFromJustToken(String justToken){
        return (String) getJwtBodyFromJustToken(justToken).get(NICKNAME);
    }

    public String parseJustTokenFromRequestBy(TYPE type, HttpServletRequest request) {
        String fullToken = "";
        if(type.equals(TYPE.ACCESS)) fullToken = request.getHeader(accessName);
        else if(type.equals(TYPE.REFRESH)) fullToken =request.getHeader(refreshName);
        if (StringUtils.hasText(fullToken)
                &&
            fullToken.startsWith(Objects.requireNonNull(tokenType))
        )
            return fullToken.split(ONE_BLOCK)[1]; // e부터 시작하는 jwt 토큰
        return null;
    }

    // "Bearer eyi35..." 로 부터 "Bearer " 이하만 떼어내는 메서드
    public String parseJustTokenFromFullToken(String fullToken) {
        if (StringUtils.hasText(fullToken)
                &&
                fullToken.startsWith(Objects.requireNonNull(tokenType))
        )
            return fullToken.split(ONE_BLOCK)[1]; // e부터 시작하는 jwt 토큰
        return null;
    }
}
