package cn.ghy.liumall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WeChatLogin{
    private final String openId;
    private final String sessionKey;
    private final String unionId;

    public WeChatLogin(String openId,
                       String sessionKey,
                       String unionId) {
        this.openId = openId;
        this.sessionKey = sessionKey;
        this.unionId = unionId;
    }

    public static WeChatLogin create(String userInfo) {

        int openidStart = userInfo.indexOf("openid")+10;
        int openidEnd = userInfo.indexOf("\"",openidStart);
        String openid= userInfo.substring(openidStart,openidEnd);
        int sessionkeyStart = userInfo.indexOf("session_key")+15;
        int sessionkeyEnd = userInfo.indexOf("\"",sessionkeyStart);
        String sessionkey = userInfo.substring(sessionkeyStart,sessionkeyEnd);
        int unionidStart = userInfo.indexOf("unionid");
        String unionid = null;
        if (unionidStart > -1){
            unionidStart = unionidStart + 11;
            int unionidEnd = userInfo.indexOf("\"",unionidStart);
            unionid = userInfo.substring(unionidStart,unionidEnd);
        }

        return new WeChatLogin(openid, sessionkey, unionid);
    }

    public String getOpenId() {
        return openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }
}
