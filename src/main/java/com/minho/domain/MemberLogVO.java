package com.minho.domain;

import lombok.Data;

@Data
public class MemberLogVO {
    private Integer member_log_id;
    private String email;
    private String type;
    private String created;
}
