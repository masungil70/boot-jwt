package org.kosa.jwt;

import java.util.Optional;

import org.kosa.jwt.dto.MemberDTO;
import org.kosa.jwt.vo.MemberVO;
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
		log.info("username = {}", username);
		Optional<MemberVO> optional = memberDAO.findByUid(username);
		MemberVO memberVO = optional.orElseThrow(() -> new UsernameNotFoundException("멤버 아이디를 찾을 수 없습니다"));
		log.info("memberVO = {}", memberVO);
		MemberDTO memberDTO = MemberDTO.of(memberVO);
		log.info("memberDTO = {}", memberDTO);
		return memberDTO;
	}

}
