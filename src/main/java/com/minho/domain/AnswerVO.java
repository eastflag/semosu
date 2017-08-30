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
    private String youtube;
    private Boolean charged;
    private Integer sort_order;
    private String created;
    private String updated;

    private QuestionVO question;
}