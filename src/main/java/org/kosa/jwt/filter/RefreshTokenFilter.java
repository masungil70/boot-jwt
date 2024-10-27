package org.kosa.jwt.filter;

import java.io.IOException;

import org.kosa.jwt.MemberDetailsService;
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
public class RefreshTokenFilter  extends OncePerRequestFilter {

    private final String refreshPath;
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
    }

}



