package com.minho.persistence;

import com.minho.domain.*;
import org.apache.ibatis.annotations.*;

import java.lang.reflect.Member;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select({"<script>",
            "SELECT member.*, auth.role from member left outer join auth on member.member_id = auth.member_id",
            "where email = #{email}",
            "</script>"})
    MemberVO selectMember(MemberVO member);

    @Insert({"<script>",
            "INSERT INTO member(email, pw, nickname, created)",
            "VALUES(#{email}, #{pw}, #{nickname}, now())",
            "</script>"})
    int insertMember(MemberVO member);

    @Select({"<script>",
            "SELECT * FROM member",
            "where email = #{email}",
            "</script>"})
    MemberVO login(MemberVO member);

    @Select({"<script>",
            "SELECT * FROM category",
            "where parent_category_id = #{parent_category_id}",
            "order by sort_order desc",
            "</script>"})
    List<CategoryVO> selectCategoryByParent(int parent_category_id);

    @Select({"<script>",
            "SELECT * FROM category",
            "where category_id = #{category_id}",
            "</script>"})
    CategoryVO selectOneCategory(int category_id);

    @Select({"<script>",
            "SELECT *, (select count(*) from answer where question_id = Q.question_id) as answer_count from question Q",
            "where category_id = #{category_id}",
            "order by sort_order asc",
            "</script>"})
    List<QuestionVO> selectQuestion(int category_id);

    @ResultMap("resultQuestion")
    @Select({"<script>",
            "select category.*, question.*",
            "from question inner join category on question.category_id = category.category_id",
            "where question_id = #{question_id}",
            "</script>"})
    QuestionVO selectOneQuestion(int question_id);

    @Select({"<script>",
            "SELECT A.*, (select count(*) from review where review.answer_id = A.answer_id) as review_count,",
            "(select avg(rate) from review where review.answer_id = A.answer_id) as review_rate",
            "FROM answer A",
            "where question_id = #{question_id}",
            "order by sort_order asc",
            "</script>"})
    List<AnswerVO> selectAnswer(int question_id);

    @Select({"<script>",
            "SELECT *",
            "FROM answer",
            "where answer_id = #{answer_id}",
            "</script>"})
    AnswerVO selectOneAnswer(int answer_id);

    // 게시판 ----------------------------------------------------------------------------------------------------------
    @Select({"<script>",
            "SELECT B.*, M.nickname, (select count(*) from  comment where board_id = B.board_id) as comment_count",
            "FROM board B inner join member M on B.member_id = M.member_id",
            "WHERE board_type=#{board_type}",
            "ORDER BY B.board_id desc",
            "LIMIT #{start_index}, #{page_size}",
            "</script>"})
    List<BoardVO> selectBoardList(BoardVO board);

    @Select({"<script>",
            "SELECT count(*) FROM board",
            "where board_type=#{board_type}",
            "</script>"})
    int countBoard(BoardVO board);

    @Insert({"<script>",
            "INSERT INTO board(board_type, member_id, title, content, created)",
            "VALUES(#{board_type}, #{member_id}, #{title}, #{content}, now())",
            "</script>"})
    int insertBoard(BoardVO board);

    @Select({"<script>",
            "SELECT board.*, member.nickname FROM board inner join member on board.member_id = member.member_id",
            "where board_id=#{board_id}",
            "</script>"})
    BoardVO selectBoard(int board_id);

    @Update({"<script>",
            "UPDATE board",
            "set title = #{title}, content = #{content}",
            "WHERE board_id = #{board_id}",
            "</script>"})
    int updateBoard(BoardVO board);

    @Delete({"<script>",
            "DELETE FROM board",
            "WHERE board_id = #{board_id}",
            "</script>"})
    int deleteBoard(int board_id);

    // 댓글 ----------------------------------------------------------------------------------------------------------
    @Select({"<script>",
            "SELECT comment.*, member.nickname FROM comment inner join member on comment.member_id = member.member_id",
            "where board_id=#{board_id}",
            "order by comment_id desc",
            "</script>"})
    List<CommentVO> selectCommentList(int board_id);

    @Insert({"<script>",
            "INSERT INTO comment(board_id, member_id, content, created)",
            "VALUES(#{board_id}, #{member_id}, #{content}, now())",
            "</script>"})
    int insertComment(CommentVO comment);

    @Update({"<script>",
            "UPDATE comment",
            "set content = #{content}",
            "WHERE comment_id = #{comment_id}",
            "</script>"})
    int updateComment(CommentVO comment);

    @Delete({"<script>",
            "DELETE FROM comment",
            "WHERE comment_id = #{comment_id}",
            "</script>"})
    int deleteComment(int comment_id);

    // 리뷰 ----------------------------------------------------------------------------------------------------------
    @Select({"<script>",
            "SELECT review.*, member.nickname FROM review inner join member on review.member_id = member.member_id",
            "where answer_id=#{answer_id}",
            "order by review_id desc",
            "</script>"})
    List<ReviewVO> selectReviewList(int answer_id);

    @Insert({"<script>",
            "INSERT INTO review(answer_id, member_id, rate, content, created)",
            "VALUES(#{answer_id}, #{member_id}, #{rate}, #{content}, now())",
            "</script>"})
    int insertReview(ReviewVO review);

    @Update({"<script>",
            "UPDATE review",
            "set rate = #{rate}, content = #{content}",
            "WHERE review_id = #{review_id}",
            "</script>"})
    int updateReview(ReviewVO review);

    @Delete({"<script>",
            "DELETE FROM review",
            "WHERE review_id = #{review_id}",
            "</script>"})
    int deleteReview(int review_id);
}
