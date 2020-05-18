package org.javaboy.vhr.config;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

//后端接口权限设计
//根据用户传来的请求地址，分析请求需要的角色，这是第一步
//第二步分析
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    MenuService menuService;
    //用来匹配的类
    AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    //Collection为当前请求所需的角色
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //o实际上是一个FilterInvocation对象
        String requestUrl=((FilterInvocation) o).getRequestUrl();
        List<Menu>menus= menuService.getAllMenusWithRole();
        for (Menu menu : menus) {
            //进行匹配，模式为menu里的url，匹配url
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                //当前请求所需的角色
                List<Role>roles=menu.getRoles();
                String[]str=new String[roles.size()];
                for (int i=0;i<roles.size();i++){
                    //list的get方法返回一个role，再根据role里的getName方法获取角色名
                    str[i]=roles.get(i).getName();
                }
                //SecurityConfig.createList这里是springsecurity的，参数为角色的名字
                return SecurityConfig.createList(str);
            }
        }
        //没匹配上，登陆访问即可，ROLE_LOGIN仅为一个标记
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
