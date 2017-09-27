package com.minho.persistence;

import com.minho.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by eastflag on 2017-08-17.
 */
@Mapper
public interface AdminMapper {
    // 카테고리 관리 -------------------
    @Select({"<script>",
            "SELECT p.*, (select count(*) from category c where c.parent_category_id = p.category_id) as count FROM category p",
            "where category_level = 0",
            "</script>"})
    List<CategoryVO> selectRootCategory();

    @Select({"<script>",
            "SELECT p.*, (select count(*) from category c where c.parent_category_id = p.category_id) as count FROM category p",
            "where parent_category_id = #{parent_category_id}",
            "order by sort_order asc",
            "</script>"})
    List<CategoryVO> selectCategory(int parent_category_id);

    @Select({"<script>",
            "SELECT * FROM category",
            "where category_id = #{category_id}",
            "</script>"})
    CategoryVO selectOneCategory(CategoryVO category);

    @Options(useGeneratedKeys = true, keyProperty = "category_id")
    @Insert({"<script>",
            "INSERT INTO category(parent_category_id, name, category_level, sort_order, test_date, grade_cut, created)",
            "VALUES(#{parent_category_id}, #{name}, #{category_level}, #{sort_order}, #{test_date}, #{grade_cut}, now())",
            "</script>"})
    int insertCategory(CategoryVO category);

    @Update({"<script>",
            "update category",
            "<trim prefix='set' suffixOverrides=','>",
            "<if test='parent_category_id!=null'>parent_category_id = #{parent_category_id},</if>",
            "<if test='name!=null'>name = #{name},</if>",
            "<if test='category_level!=null'>category_level = #{category_level},</if>",
            "<if test='sort_order!=null'>sort_order = #{sort_order},</if>",
            "<if test='test_date!=null'>test_date = #{test_date},</if>",
            "<if test='grade_cut!=null'>grade_cut = #{grade_cut},</if>",
            "</trim>",
            "WHERE category_id=#{category_id}",
            "</script>"})
    int updateCategory(CategoryVO category);

    @Delete({"<script>",
            "delete from category",
            "WHERE category_id=#{category_id}",
            "</script>"})
    int deleteCategory(int category_id);

    // 문제관리 ----------------------------------------------------------------------
    @Select({"<script>",
            "SELECT *, (select count(*) from answer where question_id = Q.question_id) as answer_count from question Q",
            "where category_id = #{category_id}",
            "order by sort_order asc",
            "LIMIT #{start_index}, #{page_size}",
            "</script>"})
    List<QuestionVO> selectQuestion(QuestionVO question);

    @Select({"<script>",
            "SELECT count(*) from question",
            "where category_id = #{category_id}",
            "</script>"})
    int selectQuestionCount(QuestionVO question);

    @Select({"<script>",
            "SELECT * from question",
            "where question_id = #{question_id}",
            "</script>"})
    QuestionVO selectOneQuestion(QuestionVO question);

    @Options(useGeneratedKeys = true, keyProperty = "question_id")
    @Insert({"<script>",
            "INSERT INTO question(category_id, number, sort_order, content, distribution, correct_rate, image, created)",
            "VALUES(#{category_id}, #{number}, #{sort_order}, #{content}, #{distribution}, #{correct_rate}, #{image}, now())",
            "</script>"})
    int insertQuestion(QuestionVO question);

    @Update({"<script>",
            "update question",
            "<trim prefix='set' suffixOverrides=','>",
            "<if test='category_id!=null'>category_id = #{category_id},</if>",
            "<if test='number!=null'>number = #{number},</if>",
            "<if test='sort_order!=null'>sort_order = #{sort_order},</if>",
            "<if test='content!=null'>content = #{content},</if>",
            "<if test='distribution!=null'>distribution = #{distribution},</if>",
            "<if test='correct_rate!=null'>correct_rate = #{correct_rate},</if>",
            "<if test='image!=null'>image = #{image},</if>",
            "</trim>",
            "WHERE question_id=#{question_id}",
            "</script>"})
    int updateQuestion(QuestionVO question);

    @Delete({"<script>",
            "delete from question",
            "WHERE question_id=#{question_id}",
            "</script>"})
    int deleteQuestion(int question_id);

    // 정답관리 --------------------------------------------------
    @Select({"<script>",
            "SELECT * from answer",
            "where question_id = #{question_id}",
            "order by sort_order asc",
            "</script>"})
    List<AnswerVO> selectAnswer(int question_id);

    @Select({"<script>",
            "SELECT * from answer",
            "where answer_id = #{answer_id}",
            "</script>"})
    AnswerVO selectOneAnswer(AnswerVO answer);

    @Options(useGeneratedKeys = true, keyProperty = "answer_id")
    @Insert({"<script>",
            "INSERT INTO answer(question_id, teacher, title, youtube, charged, sort_order, created)",
            "VALUES(#{question_id}, #{teacher}, #{title}, #{youtube}, #{charged}, #{sort_order}, now())",
            "</script>"})
    int insertAnswer(AnswerVO answer);

    @Update({"<script>",
            "update answer",
            "<trim prefix='set' suffixOverrides=','>",
            "<if test='question_id!=null'>question_id = #{question_id},</if>",
            "<if test='teacher!=null'>teacher = #{teacher},</if>",
            "<if test='title!=null'>title = #{title},</if>",
            "<if test='youtube!=null'>youtube = #{youtube},</if>",
            "<if test='charged!=null'>charged = #{charged},</if>",
            "<if test='sort_order!=null'>sort_order = #{sort_order},</if>",
            "</trim>",
            "WHERE answer_id=#{answer_id}",
            "</script>"})
    int updateAnswer(AnswerVO answer);

    @Delete({"<script>",
            "delete from answer",
            "WHERE answer_id=#{answer_id}",
            "</script>"})
    int deleteAnswer(int answer_id);

    // 리뷰관리
    @Select({"<script>",
            "SELECT review.*, answer.teacher, answer.title",
            "FROM review inner join answer on review.answer_id = answer.answer_id",
            "order by review.updated desc",
            "LIMIT #{start_index}, #{page_size}",
            "</script>"})
    List<ReviewVO> selectReview(SearchVO search);

    @Update({"<script>",
            "update review",
            "set reply = #{reply}, reply_created = now()",
            "WHERE review_id=#{review_id}",
            "</script>"})
    int updateReview(ReviewVO review);
}
