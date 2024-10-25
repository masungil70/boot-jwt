package org.kosa.jwt.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
    @Value("${org.kosa.jwt.secret}")
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days){

        log.info("generateKey..." + key);

        return null;
    }


    public Map<String, Object> validateToken(String token)throws JwtException {

        Map<String, Object> claim = null;

        return claim;
    }

}
