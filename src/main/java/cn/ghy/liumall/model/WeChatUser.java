package cn.ghy.liumall.model;

import java.io.Serializable;

public class WeChatUser implements Serializable {
    private Long userId;
    private String openId;
    private String sessionKey;
    private String unionId;

    public WeChatUser(){}

    public WeChatUser(String openId,
                       String sessionKey,
                       String unionId) {
        this.openId = openId;
        this.sessionKey = sessionKey;
        this.unionId = unionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
