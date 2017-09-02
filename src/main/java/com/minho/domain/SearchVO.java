package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class SearchVO {

	private String search_key;
	private String search_value;
	private Integer start_index;
	private Integer page_size;
	private Integer total_count;
	private String order_key;
}
