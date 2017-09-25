package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by eastflag on 2016-10-11.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BoardVO extends SearchVO {
    private Integer board_id;
    private String board_type;
    private Integer member_id;
    private String title;
    private String content;
    private String created;
    private String updated;

    //result 매핑
    private String nickname;
    private Integer comment_count;
}
