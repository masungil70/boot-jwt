package org.kosa.jwt;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.kosa.jwt.vo.MemberVO;

@Mapper
public interface MemberDAO {
	List<MemberVO> findAll();
	int  insert(MemberVO member);
	Optional<MemberVO> findByUid(String uid);
}
