package com.minho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minho.ConfigConstant;
import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
import com.minho.persistence.AdminMapper;
import com.minho.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by eastflag on 2017-08-17.
 */
@RestController
@RequestMapping("/admin/api")
public class AdminController {
    @Autowired
    private ConfigConstant configConstant;

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
    public Result addQuestion(@RequestPart(value="file", required = false) MultipartFile file, @RequestPart("json") String inQuestion) {
        QuestionVO question = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            question = mapper.readValue(inQuestion, QuestionVO.class);

            // 이미지가 있는지 체크
            if (file != null) {
                //업로드할 디렉토리가 있는지 체크
                String path = configConstant.uploadRootFolder + configConstant.uploadQuestionFolder;
                File dir = new File(path);
                if (!dir.isDirectory()) {
                    dir.mkdirs();
                }
                // 파일 정보 추출
                String filename = file.getOriginalFilename();
                String pointExt = filename.substring(filename.lastIndexOf("."));

                // DB 저장하여 question_id 추출
                question.setImage(pointExt);
                adminMapper.insertQuestion(question);

                //파일 저장
                String savedFilename = String.format("%d%s", question.getQuestion_id(), pointExt);
                File saveFile = new File(path, savedFilename);
                file.transferTo(saveFile);
            } else {
                // 이미지가 없으면 DB만 저장
                adminMapper.insertQuestion(question);
                return new Result(0, "success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(500, "internal server error");
    }

    @PutMapping(value="/question")
    public Result modifyQuestion(@RequestPart(value="file", required = false) MultipartFile file, @RequestPart("json") String inQuestion) {
        QuestionVO question = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            question = mapper.readValue(inQuestion, QuestionVO.class);

            // 이미지가 있는지 체크
            if (file != null) {
                // 파일 정보 추출
                String path = configConstant.uploadRootFolder + configConstant.uploadQuestionFolder;
                String filename = file.getOriginalFilename();
                String pointExt = filename.substring(filename.lastIndexOf("."));

                // DB 수정
                question.setImage(pointExt);
                adminMapper.updateQuestion(question);

                //파일 저장
                String savedFilename = String.format("%d%s", question.getQuestion_id(), pointExt);
                File saveFile = new File(path, savedFilename);
                file.transferTo(saveFile);
            } else {
                adminMapper.updateQuestion(question);
                new Result(0, "success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(500, "internal server error");
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
