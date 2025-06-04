package org.kosa.jwt.controller;


import java.util.List;

import org.kosa.jwt.MemberDAO;
import org.kosa.jwt.dto.MemberDTO;
import org.kosa.jwt.vo.MemberVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "api version1", description = "RestApiContrroller 관련 API 입니다.")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
//@CrossOrigin("http://localhost:3000") // CORS 허용
public class RestApiController {

	private final MemberDAO memberDAO;
	private final PasswordEncoder passwordEncoder;

	// 모든 사람이 접근 가능
	@ApiResponse(
        responseCode = "200",
        description = "home 호출을 성공하였습니다."
	)
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}

	// 유저 혹은 매니저 혹은 어드민이 접근 가능
	@GetMapping("user")
	public String user(Authentication authentication) {
		MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
		System.out.println("uid : " + memberDTO.getUid());
		System.out.println("username : " + memberDTO.getUsername());
		System.out.println("password : " + memberDTO.getPassword());
		System.out.println("uuid : " + memberDTO.getUuid());

		return "<h1>user</h1>";
	}

	// 매니저 혹은 어드민이 접근 가능
	@GetMapping("manager/reports")
	public String reports() {
		return "<h1>reports</h1>";
	}

	// 어드민이 접근 가능
	@GetMapping("admin/users")
	public List<MemberVO> users() {
		return memberDAO.findAll();
	}

	// 모든 사람이 접근 가능
	@PostMapping("join")
	public String join(@RequestBody MemberVO memberVO) {
		memberVO.setPwd(passwordEncoder.encode(memberVO.getPwd()));
		memberVO.setRoles("ROLE_USER");
		memberDAO.insert(memberVO);
		return "회원가입완료";
	}

}

/*
 * 
 테스팅 하는 방법 
 
모든 사람이 접근 가능
curl -v http://localhost:8080/api/v1/home

회원가입완료
curl -v -X POST  -H "content-type: application/json" -d "{\"id\":10,\"username\":\"masungil\",\"password\":\"1234\"}" http://localhost:8090/api/v1/join

로그인
curl -v -X POST  -d "{\"username\":\"masungil\",\"password\":\"1234\"}" http://localhost:8090/login

유저 혹은 매니저 혹은 어드민이 접근 가능
curl -v -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoibWFzdW5naWwiLCJleHAiOjE3MTU5MTAzMTR9.DtQKTN4KsqhHQBybcUHFj8YyeSIJKE3yIuZg6G5rEco" http://localhost:8090/api/v1/user
*/