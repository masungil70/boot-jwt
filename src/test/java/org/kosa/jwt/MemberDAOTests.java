package org.kosa.jwt;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.kosa.jwt.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MemberDAOTests {
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberDAO memberDAO;
    
	@Autowired
    private MemberDetailsService memberDetailsService;

//    @Test
//    public void testInserts() {
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            MemberVO memberVO = MemberVO.builder()
//                    .uid("apiuser"+i)
//                    .pwd( passwordEncoder.encode("1111") )
//                    .build();
//
//            memberDAO.insert(memberVO);
//        });
//    }
    
    @Test
    public void testLoadUserByUserName() {
    	String username = "apiuser1";
    	try {
    		UserDetails userDetails = memberDetailsService.loadUserByUsername(username);
    		log.info(userDetails.toString());
    	} catch (UsernameNotFoundException e) {
    		e.printStackTrace();
    	}
    }

}
