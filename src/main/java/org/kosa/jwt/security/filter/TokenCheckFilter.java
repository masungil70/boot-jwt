package org.kosa.jwt.security.filter;

import java.io.IOException;

import org.kosa.jwt.util.JWTUtil;
import org.springframework.web.filter.OncePerRequestFilter;

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
		filterChain.doFilter(request, response);
	}

}
