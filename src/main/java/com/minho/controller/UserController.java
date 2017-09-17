package com.minho.controller;

import com.minho.Constant;
import com.minho.domain.AnswerVO;
import com.minho.domain.CategoryVO;
import com.minho.domain.MemberVO;
import com.minho.domain.QuestionVO;
import com.minho.persistence.UserMapper;
import com.minho.result.Result;
import com.minho.result.ResultData;
import com.minho.utils.CommonUtil;
import com.minho.utils.CryptographyPasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    //
    @GetMapping(value="/categoryByParent")
    public List<CategoryVO> findCategoryByParent(@RequestParam int parent_category_id) {
        return userMapper.selectCategoryByParent(parent_category_id);
    }

    @GetMapping(value="/category")
    public CategoryVO findOneCategory(@RequestParam int category_id) {
        return userMapper.selectOneCategory(category_id);
    }

    @GetMapping(value="/question")
    public List<QuestionVO> findQuestion(@RequestParam int category_id) {
        return userMapper.selectQuestion(category_id);
    }

    @GetMapping(value="/answer")
    public List<AnswerVO> findAnswer(@RequestParam int question_id) {
        return userMapper.selectAnswer(question_id);
    }

    //회원가입
    @Transactional
    @RequestMapping("/register")
    public Result addMember(@RequestBody MemberVO inMember) {
        inMember.setEmail(inMember.getEmail());
        //중복 체크
        MemberVO member = userMapper.selectMember(inMember);
        if (member != null) {
            return new Result(100, "등록된 이메일입니다.");
        }

        String hashPassword = CryptographyPasswordHash.computePasswordHash(inMember.getPw(), null);

        inMember.setPw(hashPassword);

        userMapper.insertMember(inMember);

        //토큰 발행
        String token;
        try {
            token = CommonUtil.createJWT(inMember.getEmail(), String.valueOf(inMember.getMember_id()),
                    inMember.getNickname(), Constant.SESSION_TIMEOUT);
            inMember.setToken(token);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(500, "서버오류가 발생하였습니다. 잠시후에 시도하세요.");
        }

        return new ResultData(0, "success", inMember);
    }

    //사용자 로그인
    @RequestMapping("/login")
    public Result login(@RequestBody MemberVO inMember, HttpServletRequest request) {
        inMember.setEmail(inMember.getEmail());
        MemberVO member = userMapper.selectMember(inMember);
        if (member == null) {
            return new Result(450, "존재하지 않는 이메일입니다.");
        }

        // logger.debug("inMember:" + inMember.getPassword() + ", member:" + member.getPassword());
        boolean result = CryptographyPasswordHash.verifyPassword(inMember.getPw(), member.getPw());

        if(result) {
            //토큰 발행
            String token;
            try {
                token = CommonUtil.createJWT(member.getEmail(), String.valueOf(member.getMember_id()),
                        member.getNickname(), Constant.SESSION_TIMEOUT);
                member.setToken(token);
                return new ResultData(0, "success", member);
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(500, "서버오류가 발생하였습니다. 잠시후에 시도하세요.");
            }
        } else {
            return new Result(451, "비밀번호가 맞지 않습니다. 다시 확인해주세요.");
        }
    }
}
