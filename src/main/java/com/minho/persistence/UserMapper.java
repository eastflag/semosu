package com.minho.persistence;

import com.minho.domain.AnswerVO;
import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
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

    @Select({"<script>",
            "SELECT * FROM answer",
            "where question_id = #{question_id}",
            "order by sort_order asc",
            "</script>"})
    List<AnswerVO> selectAnswer(int question_id);
}
