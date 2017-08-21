package com.minho.persistence;

import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
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

    // 문제관리 --------------------
    @Select({"<script>",
            "SELECT * from question",
            "where category_id = #{category_id}",
            "order by number asc",
            "</script>"})
    List<QuestionVO> selectQuestion(int category_id);

    @Options(useGeneratedKeys = true, keyProperty = "question_id")
    @Insert({"<script>",
            "INSERT INTO question(category_id, number, content, distribution, correct_rate, image, created)",
            "VALUES(#{category_id}, #{number}, #{content}, #{distribution}, #{correct_rate}, #{image}, now())",
            "</script>"})
    int insertQuestion(QuestionVO question);

    @Update({"<script>",
            "update question",
            "<trim prefix='set' suffixOverrides=','>",
            "<if test='category_id!=null'>category_id = #{category_id},</if>",
            "<if test='number!=null'>number = #{number},</if>",
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
}
