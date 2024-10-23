package org.kosa.jwt.vo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
	private String uid;
	private String pwd;
	private String name;
	private String uuid; 
	private String roles; 
	
	public boolean isEqualPwd(String pwd) {
		return this.pwd.equals(pwd);
	}
	
	public void changePwd(String pwd) {
		if (!Objects.equals(pwd, pwd)) {
			this.pwd = pwd;
		}
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
		if (roles != null && roles.length() > 0) {
			return Arrays.stream(roles.split(",")).map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toUnmodifiableList());
		}
		return Collections.EMPTY_LIST;
	}
}
