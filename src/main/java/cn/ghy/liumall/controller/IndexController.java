package cn.ghy.liumall.controller;

import cn.ghy.liumall.model.WeChatLogin;
import cn.ghy.liumall.security.HttpsRequest;
import cn.ghy.liumall.security.JwtTokenProvider;
import cn.ghy.liumall.service.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URL;

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

    @RequestMapping(value="/signin", method= RequestMethod.POST)
    @ResponseBody
//    receive code from client get user information from wechat server
    public ApiResponse authenticateUser(@Valid @RequestBody String code){
        String wechatServer = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code";
        String loginInfo = HttpsRequest.httpsRequest(wechatServer,"GET",null);
        if (loginInfo.indexOf("errcode") < 0){
            return new ApiResponse(false,"login failed");
        }else{
            WeChatLogin weChatLogin = WeChatLogin.create(loginInfo);
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
}
