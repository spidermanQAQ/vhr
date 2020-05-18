package org.javaboy.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    HrService hrService;

    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    CustomUrlDecisionManager myDecisionManager;

    @Autowired
    LoginFilter loginFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //告诉springsecurity userdetail已经准备好了，你可以拿去用了
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Override
    //访问login不用经过springSecurity拦截，否则
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login","/verify","/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }

    @Override
    //这个重载可以用来配置拦截规则，不同权限对象有不同的登陆规则
    protected void configure(HttpSecurity http) throws Exception {
        //注意上面要放行
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        //开启配置
        http.authorizeRequests()
                //表示剩余的其他接口，登录之后就能访问
                /*.anyRequest().authenticated()*/
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(myDecisionManager);
                        o.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return o;
                    }
                })
                //and 方法表示结束当前标签，上下文回到HttpSecurity，开启新一轮的配置
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                //登录处理接口
                .loginProcessingUrl("/doLogin")
                //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                /*.loginPage("/login")*/
                //登录成功的处理器
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    //authentication保存着登陆成功的用户信息，是在登录请求时就记录下了，在hrservice认证用户名，密码后进行填充权限信息
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        //响应的内容类型以及编码方式
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        //登陆成功的hr对象
                        Hr hr = (Hr) authentication.getPrincipal();
                        //不把密码显示出来
                        hr.setPassword(null);
                        RespBean ok = RespBean.ok("登陆成功", hr);
                        //利用jackson的ObjectMapper的writeValueAsString（）方法将hr对象写成字符串
                        String s = new ObjectMapper().writeValueAsString(ok);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        //响应的内容类型以及编码方式
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        RespBean respBean = RespBean.error("登录失败");
                        //对象是不是一个类的实例
                        if (exception instanceof LockedException) {
                            respBean.setMsg("账户被锁定，请联系管理员");
                        } else if (exception instanceof CredentialsExpiredException) {
                            respBean.setMsg("密码过期，请联系管理员");
                        } else if (exception instanceof AccountExpiredException) {
                            respBean.setMsg("账户过期,请联系管理员");
                        } else if (exception instanceof BadCredentialsException) {
                            respBean.setMsg("用户名或密码错误，请重新输入");
                        }
                        ;
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                //关闭csrf攻击，为了postman侧式
                .csrf().disable()
                .exceptionHandling()
                //没有认证时，在这里处理结果，不要重定向
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        //解决session失效造成的登陆问题
                        resp.setStatus(401);
                        PrintWriter printWriter = resp.getWriter();
                        RespBean respBean = RespBean.error("访问失败");
                        if (e instanceof InsufficientAuthenticationException) {
                            respBean.setMsg("请求失败，请联系管理员");
                        }
                        printWriter.write(new ObjectMapper().writeValueAsString(respBean));
                        printWriter.flush();
                        printWriter.close();
                    }
                });
    }
}
