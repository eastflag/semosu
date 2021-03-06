package com.minho.controller;

import com.minho.Constant;
import com.minho.domain.*;
import com.minho.persistence.UserMapper;
import com.minho.result.Result;
import com.minho.result.ResultData;
import com.minho.result.ResultDataTotal;
import com.minho.utils.CommonUtil;
import com.minho.utils.CryptographyPasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    //회원가입
    /*@Transactional
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
    }*/

    // 연도별 문제---------------------------------------------------------------------------------------------------------
    @GetMapping(value="/categoryByParent")
    public List<CategoryVO> findCategoryByParent(@RequestParam int parent_category_id) {
        return userMapper.selectCategoryByParent(parent_category_id);
    }

    @GetMapping(value="/category")
    public CategoryVO findOneCategory(@RequestParam int category_id) {
        CategoryVO category = userMapper.selectOneCategory(category_id);
        category.setParent_name(userMapper.selectRootLevel(category_id));
        return category;
    }

    @GetMapping(value="/question")
    public List<QuestionVO> findQuestion(@RequestParam int category_id) {
        return userMapper.selectQuestion(category_id);
    }

    @GetMapping(value="/oneQuestion")
    public QuestionVO findOneQuestion(@RequestParam int question_id) {
        return userMapper.selectOneQuestion(question_id);
    }

    @GetMapping(value="/answer")
    public List<AnswerVO> findAnswer(@RequestParam int question_id) {
        return userMapper.selectAnswer(question_id);
    }

    @GetMapping(value="/oneAnswer")
    public AnswerVO findOneAnswer(@RequestParam int answer_id) {
        return userMapper.selectOneAnswer(answer_id);
    }

    //게시판 -----------------------------------------------------------------------------------------------------------
    @PostMapping("/boardList")
    public ResultDataTotal<List<BoardVO>> getBoardList(@RequestBody BoardVO board) {
        return new ResultDataTotal<>(0, "success", userMapper.selectBoardList(board), userMapper.countBoard(board)) ;
    }

    @PostMapping("/board")
    public Result addBoard(@RequestBody BoardVO board) {
        userMapper.insertBoard(board);
        return new Result(0, "success");
    }

    @GetMapping("/board")
    public BoardVO getBoard(@RequestParam int board_id) {
        return userMapper.selectBoard(board_id);
    }

    @PutMapping("/board")
    public Result modifyBoard(@RequestBody BoardVO board) {
        userMapper.updateBoard(board);
        return new Result(0, "success");
    }

    @DeleteMapping("/board")
    public Result removeBoard(@RequestParam int board_id) {
        userMapper.deleteBoard(board_id);
        return new Result(0, "success");
    }

    //댓글--------------------------------------------------------------------------------------------------------------
    @GetMapping("/comment")
    public List<CommentVO> getComment(@RequestParam int board_id) {
        return userMapper.selectCommentList(board_id);
    }

    @PostMapping("/comment")
    public Result addComment(@RequestBody CommentVO comment) {
        userMapper.insertComment(comment);
        return new Result(0, "success");
    }

    @PutMapping("/comment")
    public Result modifyComment(@RequestBody CommentVO comment) {
        userMapper.updateComment(comment);
        return new Result(0, "success");
    }

    @DeleteMapping("/comment")
    public Result removeComment(@RequestParam int comment_id) {
        userMapper.deleteComment(comment_id);
        return new Result(0, "success");
    }

    // 정답 리뷰--------------------------------------------------------------------------------------------------------
    @GetMapping("/review")
    public List<ReviewVO> getreview(@RequestParam int answer_id) {
        return userMapper.selectReviewList(answer_id);
    }

    @PostMapping("/review")
    public Result addreview(@RequestBody ReviewVO review) {
        userMapper.insertReview(review);
        return new Result(0, "success");
    }

    @PutMapping("/review")
    public Result modifyreview(@RequestBody ReviewVO review) {
        userMapper.updateReview(review);
        return new Result(0, "success");
    }

    @DeleteMapping("/review")
    public Result removereview(@RequestParam int review_id) {
        userMapper.deleteReview(review_id);
        return new Result(0, "success");
    }

    // 문제 리뷰--------------------------------------------------------------------------------------------------------
    @GetMapping("/debate")
    public List<DebateVO> getdebate(@RequestParam int question_id) {
        return userMapper.selectDebateList(question_id);
    }

    @PostMapping("/debate")
    public Result adddebate(@RequestBody DebateVO debate) {
        userMapper.insertDebate(debate);
        return new Result(0, "success");
    }

    @PutMapping("/debate")
    public Result modifydebate(@RequestBody DebateVO debate) {
        userMapper.updateDebate(debate);
        return new Result(0, "success");
    }

    @DeleteMapping("/debate")
    public Result removedebate(@RequestParam int debate_id) {
        userMapper.deleteDebate(debate_id);
        return new Result(0, "success");
    }

    // 평점-------------------------------------------------------------------------------------------------------------
    @GetMapping("/rate_total")
    public RateTotalVO findRateTotal(@RequestParam int answer_id) {
        return userMapper.selectRateTotal(answer_id);
    }

    @GetMapping("/rate")
    public RateVO findRate(@RequestParam int answer_id, @RequestParam int member_id) {
        RateVO rate = new RateVO();
        rate.setAnswer_id(answer_id);
        rate.setMember_id(member_id);
        rate = userMapper.selectRate(rate);

        return rate;

    }

    // insert or update
    @PostMapping("/rate")
    public Result saveRate(@RequestBody RateVO rate) {
        int result = userMapper.updateRate(rate);
        if (result == 0) {
            userMapper.insertRate(rate);
        }
        return new Result(0, "success");
    }

    //즐겨찾기----------------------------------------------------------------------------------------------------------
    @GetMapping("/oneFavorite")
    public FavoriteVO findOneFavorite(@RequestParam int member_id, @RequestParam int answer_id) {
        FavoriteVO favorite = new FavoriteVO();
        favorite.setMember_id(member_id);
        favorite.setAnswer_id(answer_id);
        return userMapper.selectOneFavorite(favorite);
    }

    @PutMapping("/favorite")
    public FavoriteVO togglefavorite(@RequestBody FavoriteVO favorite) {
        if (favorite.getExist() == null) {
            userMapper.insertFavorite(favorite);
        } else {
            // 즐겨찾기를 반대로 toggle 한다.
            favorite.setExist(!favorite.getExist());
            userMapper.updateFavorite(favorite);
        }
        return userMapper.selectOneFavorite(favorite);
    }

    @PostMapping("/favorite")
    public Result findFavorite(@RequestBody FavoriteVO favorite) {
        return new ResultDataTotal<>(0, "success",userMapper.selectFavorite(favorite),
                userMapper.countFavorite(favorite));
    }
}
