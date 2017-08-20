package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by eastflag on 2017-08-20.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class QuestionVO {
    private Integer question_id;
    private Integer category_id;
    private Integer number;
    private String content;
    private Integer distribution;
    private Integer correct_rate;
    private String image;
    private String created;
    private String updated;

    private CategoryVO category;
}
