package com.minho.domain;

import lombok.Data;

/**
 * Created by eastflag on 2017-10-02.
 */
@Data
public class FavoriteVO extends SearchVO {
    private Integer favorite_id;
    private Integer member_id;
    private Integer answer_id;
    private Boolean exist;
    private String updated;

    //result map
    private Integer question_id;
    private String name;
    private String number;
    private String content;
    private String teacher;
    private String title;
}
