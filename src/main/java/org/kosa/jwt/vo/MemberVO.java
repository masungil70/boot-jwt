package org.kosa.jwt.vo;

import java.util.Objects;

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
	
	public boolean isEqualPwd(String pwd) {
		return this.pwd.equals(pwd);
	}
	
	public void changePwd(String pwd) {
		if (!Objects.equals(pwd, pwd)) {
			this.pwd = pwd;
		}
	}
}
