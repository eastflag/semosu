package com.minho.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minho.ConfigConstant;
import com.minho.Constant;
import com.minho.domain.AuthVO;
import com.minho.domain.MemberVO;
import com.minho.domain.SocialVO;
import com.minho.persistence.LoginMapper;
import com.minho.result.Result;
import com.minho.result.ResultData;
import com.minho.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private ConfigConstant configConstant;

    @Autowired
    private LoginMapper loginMapper;

    @PostMapping(value="/api/signUp")
    public MemberVO signup(@RequestBody MemberVO member) {
        loginMapper.insertMember(member);
        getToken(member);
        return member;
    }

    @PostMapping(value="/api/login")
    public Result login(@RequestBody MemberVO inMember) {
        MemberVO member = loginMapper.selectMember(inMember);
        if (member == null) {
            return new Result(100, "do not exist");
        } else {
            getToken(member);
            return new ResultData<>(0, "success", member);
        }
    }

    @RequestMapping("/api/social")
    public SocialVO getSocialLogin(@RequestParam String site) {
        SocialVO social = new SocialVO();

        if ("naver".equals(site)) {
            SecureRandom random = new SecureRandom();
            String state = new BigInteger(130, random).toString(32);
            String url = "https://nid.naver.com/oauth2.0/authorize?client_id=r0hXyiQbbJwWmmy8nq2U" +
                    "&response_type=code&redirect_uri=http://" + configConstant.backendHost + "/naver_callback&state=" + state;
            social.setUrl(url);
        }

        return social;
    }

    @RequestMapping("/naver_callback")
    public String naverLogin(@RequestParam String code, @RequestParam String state, HttpSession session) {
        //access token 조회--------------------------------------------------------------
        RestTemplate restTemplate = new RestTemplate();
        //String authUri = "https://nid.naver.com/oauth2.0/authorize";
        String tokenUri = "https://nid.naver.com/oauth2.0/token";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", "7aWy98Ywds8IV1NEXUAL");
        parameters.add("client_secret", "fa1vHElxst");
        parameters.add("grant_type", "authorization_code");
        parameters.add("state", state);
        parameters.add("code", code);

        String token =  restTemplate.postForObject(tokenUri, parameters, String.class);
        System.out.println("token:" + token);

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(token).getAsJsonObject();

        String access_token = json.get("access_token").getAsString();
        String refresh_token = json.get("refresh_token").getAsString();

        System.out.println("access token:" + access_token);
        System.out.println("refresh_token:" + refresh_token);

        //프로파일 정보 조회--------------------------------------------------------------
        String profileUri = "https://openapi.naver.com/v1/nid/me";
        parameters.clear();
        parameters.add("", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(profileUri, HttpMethod.GET, entity, String.class);
        System.out.println("response:" + response.getBody());
        //{"resultcode":"00","message":"success","response":{"email":"leesott@naver.com","nickname":"lee****",
        // "enc_id":"06ced2e089c040b245f0944c7e50cfba2243c6688cf26553d6822c6744f1cafd","profile_image":"https:\/\/ssl.pstatic.net\/static\/pwe\/address\/nodata_33x33.gif",
        // "age":"40-49","gender":"M","id":"36112080","name":"\uc774\ub3d9\uae30","birthday":"08-26"}}

        //resultcode가 00이면
        //member table에서 이메일로 검색해서 없으면 회원가입을 시키고, 있으면 쿼리한후, 자체 토큰을 생성하여 리턴한다.

        //jwt를 생성하여 client에게 넘겨준다.
        JsonObject body = parser.parse(response.getBody()).getAsJsonObject();
        JsonObject responseJson = body.getAsJsonObject("response");
        String email = responseJson.get("email").getAsString();
        String photo_url = responseJson.get("profile_image").getAsString();
        //String name = responseJson.get("name").getAsString();

        MemberVO inMember = new MemberVO();
        inMember.setEmail(email);
        inMember.setJoin_path("naver");
        inMember.setPhoto_url(photo_url);

        MemberVO member = loginMapper.selectMember(inMember);
        if (member == null) {
            return String.format("redirect:http://%s/login?result=100", configConstant.frontendHost);
        } else {
            getToken(member);
            return String.format("redirect:http://%s/login?result=0&token=%s", configConstant.frontendHost, member.getToken());
        }
    }

    /**
     * 토큰 발행: id:  member_id, issuer: email, subject: role
     * @param member
     */
    private void getToken(MemberVO member) {
        // 토큰
        String token;
        try {
            List<AuthVO> authList = loginMapper.selectAuth(member);
            StringBuffer role = new StringBuffer();
            if (authList != null && authList.size() > 0) {
                for(AuthVO auth : authList) {
                    role.append(",");
                    role.append(auth.getRole());
                }
                role.deleteCharAt(0);
            }

            token = CommonUtil.createJWT(String.valueOf(member.getMember_id()), member.getEmail(),
                    role.toString(), Constant.SESSION_TIMEOUT);
            member.setToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
