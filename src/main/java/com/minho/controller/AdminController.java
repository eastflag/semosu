package com.minho.controller;

import com.minho.domain.CategoryVO;
import com.minho.persistence.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by eastflag on 2017-08-17.
 */
@RestController
@RequestMapping("/admin/api")
public class AdminController {
    @Autowired
    private AdminMapper adminMapper;

    @GetMapping(value="/category")
    public List<CategoryVO> getCategoryList() {
        List<CategoryVO> categoryList = adminMapper.selectRootCategory();
        return getChildren(categoryList);
    }

    private List<CategoryVO> getChildren(List<CategoryVO> categoryList) {
        for(CategoryVO category: categoryList) {
            if(category.getCount() > 0) {
                category.setChildren(getChildren(adminMapper.selectCategoryTree(category.getCategory_id())));
            }
        }
        return categoryList;
    }
}
