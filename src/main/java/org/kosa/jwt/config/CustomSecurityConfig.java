package org.kosa.jwt.config;


import org.kosa.jwt.MemberDetailsService;
import org.kosa.jwt.filter.LoginFilter;
import org.kosa.jwt.filter.RefreshTokenFilter;
import org.kosa.jwt.filter.TokenCheckFilter;
import org.kosa.jwt.util.JWTUtil;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Configuration
@Log4j2
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

	
	private final ObjectMapper objectMapper;
	private final JWTUtil jwtUtil;
	private final MemberDetailsService memberDetailsService;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");

        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());


    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        log.info("------------configure-------------------");

        //인증관리자 빌더 객체 얻기  
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        
        //인증관리자에 userDetailsService와 비밀번호 암호화 객체를 설정한다   
        authenticationManagerBuilder
        	.userDetailsService(memberDetailsService)
        	.passwordEncoder(passwordEncoder());
        
        //인증관리자를 생성한다 
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        
        //http 보안 객체에 인증관리자를 설정한다 
        http.authenticationManager(authenticationManager);
        
        //해당 소스 작성후 : 브라우저에서 /generateToken URL을 실행한다
        final LoginFilter loginFilter = new LoginFilter("/generateToken", objectMapper, jwtUtil);
        loginFilter.setAuthenticationManager(authenticationManager);
        
        //UsernamePasswordAuthenticationFilter 필더 객체 실행 전에 동작할 loginFilter를 설정한다 
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //UsernamePasswordAuthenticationFilter 필더 객체 실행 전에 동작할 TokenCheckFilter 객체를 생성하여 설정한다
        //해당 소스 작성후 : 브라우저에서 /api/sample/test URL을 실행한다
        http.addFilterBefore(new TokenCheckFilter(jwtUtil, memberDetailsService), UsernamePasswordAuthenticationFilter.class);
        
        //TokenCheckFilter 필더 객체 실행 전에 동작할 RefreshTokenFilter 객체를 생성하여 설정한다
        //해당 소스 작성후 : 브라우저에서 /refreshToken URL을 실행한다
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), TokenCheckFilter.class);

        //csrf 비활성화 
        http.csrf(csrf -> csrf.disable());
        //세션을 사용하지 않음  
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}
