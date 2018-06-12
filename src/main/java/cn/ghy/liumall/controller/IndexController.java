package cn.ghy.liumall.controller;

import cn.ghy.liumall.mapper.RoleMapper;
import cn.ghy.liumall.mapper.WeChatUserMapper;
import cn.ghy.liumall.model.WeChatLogin;
import cn.ghy.liumall.model.WeChatUser;
import cn.ghy.liumall.security.HttpsRequest;
import cn.ghy.liumall.security.JwtTokenProvider;
import cn.ghy.liumall.service.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/guest")
public class IndexController {
    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.appsecret}")
    private String appSecret;

    @Value("${app.jwtExpirationInMs.signin}")
    private int signinExpirationInMs;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    WeChatUserMapper weChatUserMapper;

    @Autowired
    RoleMapper roleMapper;

    @RequestMapping(value="/signin", method= RequestMethod.POST)
    @ResponseBody
//    receive code from client get user information from wechat server
    public ApiResponse authenticateUser(@Valid @RequestBody String code){
        String wechatServer = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code";
        String loginInfo = HttpsRequest.httpsRequest(wechatServer,"GET",null);
        if (loginInfo.contains("errcode")){
            return new ApiResponse(false,"login failed");
        }else{
            WeChatLogin weChatLogin = WeChatLogin.create(loginInfo);
            if (weChatUserMapper.findUserByOpenId(weChatLogin.getOpenId())==null){
                WeChatUser weChatUser = new WeChatUser((long)1,weChatLogin.getOpenId(),weChatLogin.getSessionKey(),weChatLogin.getUnionId());
                weChatUserMapper.insert(weChatUser);
                roleMapper.insert(weChatUser.getUserId(),"ROLE_USER");
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            weChatLogin.getOpenId(),
                            null
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.generateWechatToken(weChatLogin, signinExpirationInMs);
            return new ApiResponse(true,token);
        }
    }

    @RequestMapping(value="/test/signin", method= RequestMethod.POST)
    @ResponseBody
    public ApiResponse testAuthenticateUser(){
        String loginInfo = "{\"openid\": \"OPENID\",\"session_key\": \"SESSIONKEY\",\"unionid\": \"UNIONID\"}";
        WeChatLogin weChatLogin = WeChatLogin.create(loginInfo);
        if (weChatUserMapper.findUserByOpenId(weChatLogin.getOpenId())==null) {
            WeChatUser weChatUser = new WeChatUser((long) 2, weChatLogin.getOpenId(), weChatLogin.getSessionKey(), weChatLogin.getUnionId());
            weChatUserMapper.insert(weChatUser);
            roleMapper.insert(weChatUser.getUserId(), "ROLE_USER");
        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        weChatLogin.getOpenId(),
//                        null
//                )
//        );
        WeChatUser user = weChatUserMapper.findUserByOpenId(weChatLogin.getOpenId());
        //get role from database
        List<GrantedAuthority> authorities = roleMapper.findRolesByUserId(user.getUserId()).stream().map(role ->
                new SimpleGrantedAuthority(role.getRole())
        ).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, weChatLogin.getOpenId(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateWechatToken(weChatLogin, signinExpirationInMs);
        return new ApiResponse(true,token);

    }
}
