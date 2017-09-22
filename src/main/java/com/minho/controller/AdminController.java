package com.minho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minho.ConfigConstant;
import com.minho.domain.AnswerVO;
import com.minho.domain.CategoryVO;
import com.minho.domain.QuestionVO;
import com.minho.persistence.AdminMapper;
import com.minho.result.Result;
import com.minho.result.ResultDataTotal;
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
    public CategoryVO addCategory(@RequestBody CategoryVO category) {
        adminMapper.insertCategory(category);
        return adminMapper.selectOneCategory(category);
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

    @PutMapping(value="/child_category")
    public Result modifyChildCategory(@RequestBody List<CategoryVO> categoryList) {
        //
        int totalResultCnt = 0;
        for(CategoryVO category : categoryList) {
            int result = adminMapper.updateCategory(category);
            if (result > 0) totalResultCnt++;
        }

        if (totalResultCnt == categoryList.size()) {
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
    public Result findQuestion(@RequestParam int category_id, @RequestParam int start_index, @RequestParam int page_size) {
        QuestionVO question = new QuestionVO();
        question.setCategory_id(category_id);
        question.setStart_index(start_index);
        question.setPage_size(page_size);
        return new ResultDataTotal<>(0, "success",adminMapper.selectQuestion(question), adminMapper.selectQuestionCount(question));
    }

    @PostMapping(value="/question")
    public QuestionVO addQuestion(@RequestPart(value="file", required = false) MultipartFile file, @RequestPart("json") String inQuestion) {
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
            }
            return adminMapper.selectOneQuestion(question);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QuestionVO();
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
            }
            return new Result(0, "success");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(500, "IO server error");
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

    // 정답관리----------------------------------------------------------------------
    @GetMapping(value="/answer")
    public List<AnswerVO> findAnswer(@RequestParam int question_id) {
        return adminMapper.selectAnswer(question_id);
    }

    @PostMapping(value="/answer")
    public AnswerVO addAnswer(@RequestBody AnswerVO answer) {
        adminMapper.insertAnswer(answer);
        return adminMapper.selectOneAnswer(answer);
    }

    @PutMapping(value="/answer")
    public Result modifyAnswer(@RequestBody AnswerVO answer) {
        int result = adminMapper.updateAnswer(answer);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }

    @DeleteMapping(value="/answer")
    public Result removeAnswer(@RequestParam int answer_id) {
        int result = adminMapper.deleteAnswer(answer_id);
        if (result > 0) {
            return new Result(0, "success");
        } else {
            return new Result(100, "fail");
        }
    }
}
