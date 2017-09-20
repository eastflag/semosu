package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommentVO {
    private Integer comment_id;
    private Integer board_id;
    private Integer member_id;
    private String content;
    private String created;
    private String updated;

    private String nickname;
}
