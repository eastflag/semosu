package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by eastflag on 2017-08-20.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper=false)


public class QuestionVO extends SearchVO{
    private Integer question_id;
    private Integer category_id;
 /*   private Integer number;*/
    private Integer sort_order;
    private String content;
    private Integer distribution;
    private Integer correct_rate;
    private String image;
    private String answer;
    private String created;
    private String updated;

    private Integer answer_count;
    private Integer parent_question_id;
    private String parent_question_content;
    private Boolean isEdited; // 사용하지 않으나 ObjectMapper에서 입력을 객체로 변환시 필요함.

    private CategoryVO category;
}
