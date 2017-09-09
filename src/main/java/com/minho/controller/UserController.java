package com.minho.controller;

import com.minho.domain.AnswerVO;
import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
import com.minho.persistence.UserMapper;
import com.minho.result.Result;
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

    //
    @GetMapping(value="/categoryByParent")
    public List<CategoryVO> findCategoryByParent(@RequestParam int parent_category_id) {
        return userMapper.selectCategoryByParent(parent_category_id);
    }

    @GetMapping(value="/category")
    public CategoryVO findOneCategory(@RequestParam int category_id) {
        return userMapper.selectOneCategory(category_id);
    }

    @GetMapping(value="/question")
    public List<QuestionVO> findQuestion(@RequestParam int category_id) {
        return userMapper.selectQuestion(category_id);
    }

    @GetMapping(value="/answer")
    public List<AnswerVO> findAnswer(@RequestParam int question_id) {
        return userMapper.selectAnswer(question_id);
    }
}
