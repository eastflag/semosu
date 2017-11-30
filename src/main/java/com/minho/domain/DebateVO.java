package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DebateVO {
    private Integer debate_id;
    private Integer question_id;
    private Integer member_id;
    private String content;
    private String created;
    private String updated;
    private String nickname;
}
