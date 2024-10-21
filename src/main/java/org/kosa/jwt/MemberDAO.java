package org.kosa.jwt;

import org.apache.ibatis.annotations.Mapper;
import org.kosa.jwt.vo.MemberVO;

@Mapper
public interface MemberDAO {
	public int  insert(MemberVO member);
}
