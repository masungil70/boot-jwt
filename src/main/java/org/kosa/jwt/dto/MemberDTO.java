package org.kosa.jwt.dto;

import java.util.Collection;

import org.kosa.jwt.vo.MemberVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class MemberDTO extends User {
	private String uid;
	private String pwd;
	private String name;
	private String uuid; 

	private MemberDTO(String uid, String pwd, Collection<GrantedAuthority> authorities) {
		super(uid, pwd, authorities);
		this.uid = uid;
		this.pwd = pwd;
	}
	
	public static MemberDTO of(MemberVO memberVO) {
		MemberDTO memberDTO = new MemberDTO(memberVO.getUid(), memberVO.getPwd(), memberVO.getAuthorities());
		
		memberDTO.setName(memberVO.getName());
		memberDTO.setUuid(memberVO.getUuid());
		
		return memberDTO; 
	}

	@Override
	public String toString() {
		return "MemberDTO [uid=" + uid + ", pwd=" + pwd + ", name=" + name + ", uuid=" + uuid + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
