package com.minho.controller;

import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
import com.minho.persistence.AdminMapper;
import com.minho.result.Result;
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

    /**
     * 카테고리 전체 목록 가져오기
     * @return
     */
    @GetMapping(value="/root")
    public List<CategoryVO> findCategory() {
        List<CategoryVO> categoryList = adminMapper.selectRootCategory();
        return getChildren(categoryList);
    }

    @GetMapping(value="/category")
    public List<CategoryVO> findCategory(@RequestParam int parent_category_id) {
        return adminMapper.selectCategory(parent_category_id);
    }

    @PostMapping(value="/category")
    public Result addCategory(@RequestBody CategoryVO category) {
        int result = adminMapper.insertCategory(category);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    @PutMapping(value="/category")
    public Result modifyCategory(@RequestBody CategoryVO category) {
        int result = adminMapper.updateCategory(category);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    @DeleteMapping(value="/category")
    public Result removeCategory(@RequestParam int category_id) {
        int result = adminMapper.deleteCategory(category_id);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    //문제 관리--------------------
    @GetMapping(value="/question")
    public List<QuestionVO> findQuestion(@RequestParam int category_id) {
        return adminMapper.selectQuestion(category_id);
    }

    @PostMapping(value="/question")
    public Result addQuestion(@RequestBody QuestionVO question) {
        int result = adminMapper.insertQuestion(question);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    @PutMapping(value="/question")
    public Result modifyQuestion(@RequestBody QuestionVO question) {
        int result = adminMapper.updateQuestion(question);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    @DeleteMapping(value="/question")
    public Result removeQuestion(@RequestParam int question_id) {
        int result = adminMapper.deleteQuestion(question_id);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    private List<CategoryVO> getChildren(List<CategoryVO> categoryList) {
        for(CategoryVO category: categoryList) {
            if(category.getCount() > 0) {
                category.setChildren(getChildren(adminMapper.selectCategory(category.getCategory_id())));
            }
        }
        return categoryList;
    }
}
