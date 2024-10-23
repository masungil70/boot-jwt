package org.kosa.jwt;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.kosa.jwt.vo.MemberVO;

@Mapper
public interface MemberDAO {
	int  insert(MemberVO member);
	Optional<MemberVO> findByUid(String uid);
}
