package org.kosa.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.kosa.jwt.dto.MemberDTO;
import org.kosa.jwt.util.MapperUtil;
import org.kosa.jwt.vo.MemberVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

	private final MemberDAO memberDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MemberVO> optional = memberDAO.findByUid(username);
		MemberVO memberVO = optional.orElseThrow(() -> new UsernameNotFoundException("멤버 아이디를 찾을 수 없습니다"));
		return MemberDTO.of(memberVO);
	}

}
