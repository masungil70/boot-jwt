package org.kosa.jwt.util;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
    @Value("${org.kosa.jwt.secret}")
    private String key; // 서버만 알고 있는 비밀키값
	int EXPIRATION_TIME = 2 * 60 * 1000; //1분짜리 토큰,  10일 (1/1000초)
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
    
	public byte[] getSecretKey() {
		try {
			return Base64.getEncoder().encode(key.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public String generateToken(Map<String, Object> valueMap, int days){

        log.info("generateKey...  : " + key);


        //헤더 부분
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //테스트 시에는 짧은 유효 기간
        //int time = (1) * days; //테스트는 분단위로 나중에 60*24 (일)단위변경

        //10분 단위로 조정
        int time = (10) * days; //테스트는 분단위로 나중에 60*24 (일)단위변경
        
        try {
		        String jwtStr = Jwts.builder()
		                .header().add(valueMap) //헤더 부분
		                .and().claims(payloads) //payload 부분 설정
		                .issuedAt(Date.from(ZonedDateTime.now().toInstant())) //JWT 발급시간 설정 
		                .expiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant())) //만료기간 설정 
		                .signWith(Keys.hmacShaKeyFor(getSecretKey()), Jwts.SIG.HS256)
		                .compact();
		
		        log.info("jwtStr...  : " + jwtStr);
		        return jwtStr;
        } catch (Exception e) {
	        e.printStackTrace();
        }
        return null;
    }


    public Map<String, Object> validateToken(String token)throws JwtException {

        Map<String, Object> claim = null;

        return claim;
    }

}
