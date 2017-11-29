package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by eastflag on 2017-11-28.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RateVO {
    private int rate_id;
    private int answer_id;
    private int member_id;
    private int rate;
}
