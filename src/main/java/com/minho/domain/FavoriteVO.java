package com.minho.domain;

import lombok.Data;

/**
 * Created by eastflag on 2017-10-02.
 */
@Data
public class FavoriteVO {
    private Integer favorite_id;
    private Integer member_id;
    private Integer answer_id;
    private Boolean exist;
}
