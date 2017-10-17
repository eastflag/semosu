package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by eastflag on 2017-08-20.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AnswerVO {
    private Integer answer_id;
    private Integer question_id;
    private String teacher;
    private String title;
    private String youtube;
    private Boolean charged;
    private Integer sort_order;
    private String start;
    private String end;
    private String created;
    private String updated;

    private QuestionVO question;
    private Integer review_count; // 평점 갯수
    private Float review_rate;    // 평점 평균
}
