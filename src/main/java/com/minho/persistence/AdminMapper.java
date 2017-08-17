package com.minho.persistence;

import com.minho.domain.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by eastflag on 2017-08-17.
 */
@Mapper
public interface AdminMapper {
    @Select({"<script>",
            "SELECT p.*, (select count(*) from category c where c.parent_category_id = p.category_id) as count FROM category p",
            "where category_level = 1",
            "</script>"})
    List<CategoryVO> selectRootCategory();

    @Select({"<script>",
            "SELECT p.*, (select count(*) from category c where c.parent_category_id = p.category_id) as count FROM category p",
            "where parent_category_id = #{parent_category_id}",
            "</script>"})
    List<CategoryVO> selectCategoryTree(int parent_category_id);
}
