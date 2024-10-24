package org.kosa.jwt.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
	
	public LoginFilter(String defaultFilterPrrocessingUrl) {
		super(defaultFilterPrrocessingUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		log.info("LoginFilter ----------------------------- ");
		
		return null;
	}
	
	
}
