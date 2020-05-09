package org.javaboy.vhr.config;


import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

//判断当前用户具备MyFilter分析出来的角色
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    //用户的角色保存在authentication看源码，里面有一个 Collection<? extends GrantedAuthority> getAuthorities()，hr那边会查询hr的角色，返回authorities集合
    //所需要的角色保存在collection，即MyFilter的返回collection
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : collection) {
            String needRole=configAttribute.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)){
                //如果当前用户是AnonymousAuthenticationToken（匿名实例）
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登陆，请登录!");
                }else {
                    return;
                }
            }
            //获取当前登陆的角色,authentication.getAuthorities().var可以跳出前面的
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            //判断authentication是否包含collection里的一个即可，即遍历authentication，判断上面遍历collection的每一项needRole
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
