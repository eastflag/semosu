package com.minho.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MemberVO {
    private Integer member_id;
    private String email;
    private String nickname;
    private String pw;
    private String photo_url;
    private String join_path;
    private String created;
    private String updated;
    //private ArrayList<String> role;
    private String role;

    private String token;
}
