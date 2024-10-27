package org.kosa.jwt.filter;

import java.io.IOException;
import java.util.Map;

import org.kosa.jwt.MemberDetailsService;
import org.kosa.jwt.exception.AccessTokenException;
import org.kosa.jwt.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//요청을 받을 때마다 한번만 실행되는 필터(서블릿 필터와 같음)
@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final MemberDetailsService memberDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//요청 URL을 얻는다
		final String path = request.getRequestURI();
		
		//API 아니면 본래 처리를 할 수 있게 진행한다  
		if (!path.startsWith("/api/")) {
			filterChain.doFilter(request, response);
			return ;
		}

		log.info("이부분에서 JWT 토큰이 존재하고 유효한지 확인한다");
		log.info("jwtUtil = {}", jwtUtil);
		
        try{
        	//JWT 검증 및 인증 처리를 한다 
            setAuthentication(request);
            
            //요청한 부분으로 이동한다 
            filterChain.doFilter(request,response);
        }catch(AccessTokenException accessTokenException){
        	//응답으로 토큰 예외 발생시 오류를 전달한다 
            accessTokenException.sendResponseError(response);
        }
	}
	
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

    	//Header에 전달된 Authorization의 값을 얻는다 
        String headerStr = request.getHeader("Authorization");

        //Authorization의 값이 존재하지 않으면 오류 발생한다 
        if(headerStr == null || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr =  headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        }catch(MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("ExpiredJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
    
    private void setAuthentication(HttpServletRequest request) throws AccessTokenException {
    	Map<String, Object> payload = validateAccessToken(request);
        //uid
        String uid = (String)payload.get("uid");

        log.info("uid: " + uid);

        //uid에 대한 시큐리티 로그인 객체를 얻는다 
        UserDetails userDetails = memberDetailsService.loadUserByUsername(uid);

        //userDetails 객체를 사용하여 인증객체로 생성한다  
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

        //스프링 시큐리티에 인증객체를 설정한다 
        SecurityContextHolder.getContext().setAuthentication(authentication);
    	
    }
	

}
