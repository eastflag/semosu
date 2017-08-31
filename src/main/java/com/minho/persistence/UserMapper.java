package com.minho.persistence;

import com.minho.domain.CategoryVO;
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
}
