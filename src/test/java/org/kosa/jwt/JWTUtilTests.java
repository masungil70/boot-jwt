package org.kosa.jwt;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.kosa.jwt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class JWTUtilTests {
    @Autowired
    private JWTUtil jwtUtil;
    
    @Test
    public void testGenerate() {
    	Map<String, Object> claimMap = Map.of("uid", "apiuser1");
    	String jwpStr = jwtUtil.generateToken(claimMap, 1);
    	log.info(jwpStr);
    }

    @Test
    public void testValidateExpired() {
    	//유효기간이 지난 토큰
    	String token = "eyJ1aWQiOiJhcGl1c2VyMSIsImFsZyI6IkhTMjU2In0.eyJ1aWQiOiJhcGl1c2VyMSIsImlhdCI6MTcyOTk0NzA0NSwiZXhwIjoxNzI5OTQ3NjQ1fQ.lh9g4xzEhqVxGW6QXDJtjhsZ_RA7V8bV2C3wmL-SCqE";
    	Map<String, Object> claimMap = jwtUtil.validateToken(token);
    	log.info("claimMap = {}", claimMap);
    }

    @Test
    public void testValidateSuccess() {
    	//유효기간이 정상 토큰
    	String token = "eyJ1aWQiOiJhcGl1c2VyMSIsImFsZyI6IkhTMjU2In0.eyJ1aWQiOiJhcGl1c2VyMSIsImlhdCI6MTcyOTk0ODA4MCwiZXhwIjoxNzMwODEyMDgwfQ.bxXDg2bsYzxiiJrPSuySLtA5ymK8wywC7eIsurgTxxg";
    	Map<String, Object> claimMap = jwtUtil.validateToken(token);
    	log.info("claimMap = {}", claimMap);
    }
    
    @Test
    public void testAll() {
    	Map<String, Object> claimMap = Map.of("uid", "apiuser1", "email", "apiuser1@kosa.org", "name", "홍길동");
    	String token = jwtUtil.generateToken(claimMap, 1);
    	Map<String, Object> claim = jwtUtil.validateToken(token);
    	log.info("claim = {}", claim);
    }
    
}
