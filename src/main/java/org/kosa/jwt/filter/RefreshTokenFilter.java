package org.kosa.jwt.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.kosa.jwt.MemberDetailsService;
import org.kosa.jwt.util.JWTUtil;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//요청을 받을 때마다 한번만 실행되는 필터(서블릿 필터와 같음)
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenFilter  extends OncePerRequestFilter {

    private final String refreshPath;
	private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter.....");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("리플레쉬 필터 기능 실행 ..................");
        //filterChain.doFilter(request, response);
        
        //전송된 JSON에서 accessToken과 refreshToken을 얻어온다.
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request) {
        try{
    		//json으로 요청한 문자열을 얻는다
    		String jsonText = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    		//JSON 문자열을 Map 객체로 변환 한다
    		Map<String, String> jsonData = objectMapper.readValue(jsonText, Map.class);
    		log.info("jsonData = {}", jsonData);

            return jsonData;

        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}



