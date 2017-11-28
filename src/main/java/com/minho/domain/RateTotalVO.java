package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by eastflag on 2017-11-28.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RateTotalVO {
    private int avg;
    private int count;
}
