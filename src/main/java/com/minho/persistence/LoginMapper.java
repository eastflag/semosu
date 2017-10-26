package com.minho.persistence;

import com.minho.domain.AuthVO;
import com.minho.domain.MemberVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginMapper {

    @Select({"<script>",
            "SELECT * FROM member WHERE email= #{email}",
            "</script>"})
    MemberVO selectMember(MemberVO member);

    @Select({"<script>",
            "SELECT * FROM auth WHERE member_id= #{member_id}",
            "</script>"})
    List<AuthVO> selectAuth(MemberVO member);

    @Options(useGeneratedKeys = true, keyProperty = "member_id")
    @Insert({"<script>",
            "INSERT INTO member(email, nickname, photo_url, join_path, created)",
            "VALUES(#{email}, #{nickname}, #{photo_url}, #{join_path}, now())",
            "</script>"})
    int insertMember(MemberVO member);
}
