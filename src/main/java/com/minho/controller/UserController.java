package com.minho.controller;

import com.minho.domain.CategoryVO;
import com.minho.persistence.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping(value="/categoryByParent")
    public List<CategoryVO> findCategoryByParent(@RequestParam int parent_category_id) {
        return userMapper.selectCategoryByParent(parent_category_id);
    }
}
