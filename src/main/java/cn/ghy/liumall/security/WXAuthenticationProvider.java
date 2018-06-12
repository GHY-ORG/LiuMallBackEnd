package cn.ghy.liumall.security;

import cn.ghy.liumall.mapper.RoleMapper;
import cn.ghy.liumall.mapper.WeChatUserMapper;
import cn.ghy.liumall.model.WeChatLogin;
import cn.ghy.liumall.model.WeChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class WXAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        //get wechat user info from database
        WeChatUser user = weChatUserMapper.findUserByOpenId(token.getPrincipal().toString());
        //get role from database
        List<GrantedAuthority> authorities = roleMapper.findRolesByUserId(user.getUserId()).stream().map(role ->
                new SimpleGrantedAuthority(role.getRole())
        ).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(user, token.getPrincipal(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
