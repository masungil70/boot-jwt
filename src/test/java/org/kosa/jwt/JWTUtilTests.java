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

}
