package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class HrInfoController {

    @Autowired
    HrService hrService;

    @GetMapping("/hr/info")
    public Hr getHrInfo(Authentication authentication){
        return ((Hr) authentication.getPrincipal());
    }

    @PutMapping("/hr/info")
    public RespBean updateHr(@RequestBody Hr hr,Authentication authentication){
        if (hrService.updateHr(hr)==1){
            //更新完后应该要能动态更新页面hr的信息
            //更新完在调用@GetMapping("/hr/info")就更新了用户信息
            //getCredentials()密码，getAuthorities()角色·
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr,authentication.getCredentials(),authentication.getAuthorities()));
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");
    }

}
