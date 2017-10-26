package com.minho.controller;

import com.minho.Constant;
import com.minho.domain.AuthVO;
import com.minho.domain.MemberVO;
import com.minho.persistence.LoginMapper;
import com.minho.result.Result;
import com.minho.result.ResultData;
import com.minho.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private LoginMapper loginMapper;

    @PostMapping(value="/signUp")
    public MemberVO signup(@RequestBody MemberVO member) {
        loginMapper.insertMember(member);
        getToken(member);
        return member;
    }

    @PostMapping(value="/login")
    public Result login(@RequestBody MemberVO inMember) {
        MemberVO member = loginMapper.selectMember(inMember);
        if (member == null) {
            return new Result(100, "do not exist");
        } else {
            getToken(member);
            return new ResultData<>(0, "success", member);
        }
    }

    /**
     * 토큰 발행: id:  member_id, issuer: email, subject: role
     * @param member
     */
    private void getToken(MemberVO member) {
        // 토큰
        String token;
        try {
            List<AuthVO> authList = loginMapper.selectAuth(member);
            StringBuffer role = new StringBuffer();
            if (authList != null && authList.size() > 0) {
                for(AuthVO auth : authList) {
                    role.append(",");
                    role.append(auth.getRole());
                }
                role.deleteCharAt(0);
            }

            token = CommonUtil.createJWT(String.valueOf(member.getMember_id()), member.getEmail(),
                    role.toString(), Constant.SESSION_TIMEOUT);
            member.setToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
