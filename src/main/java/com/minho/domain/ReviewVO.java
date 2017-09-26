package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ReviewVO {
    private Integer review_id;
    private Integer answer_id;
    private Integer member_id;
    private Integer rate;
    private String content;
    private String created;
    private String updated;
    private String reply;
    private String reply_created;

    private String nickname;
}
