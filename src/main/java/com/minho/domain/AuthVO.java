package com.minho.domain;

import lombok.Data;

@Data
public class AuthVO {
    private Integer auth_id;
    private String role;
    private Integer member_id;
}
