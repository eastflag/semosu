package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CategoryVO {
    private Integer category_id;
    private String name;
    private Integer parent_category_id;
    private Integer category_level;
    private Integer sort_order;
    private Boolean use_flag;
    private String test_date;
    private String grade_cut;
    private String created;
    private String updated;

    private List<CategoryVO> children;
    private int count;
    private String parent_name;
}
