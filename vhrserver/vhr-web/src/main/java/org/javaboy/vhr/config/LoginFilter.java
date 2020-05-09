package org.javaboy.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.model.RespBean;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

@Component
public class LoginFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse)servletResponse;
      
        if ("POST".equalsIgnoreCase(req.getMethod())&&"/doLogin".equals(req.getServletPath())){
            String code = req.getParameter("code");
            String verify_code =(String) req.getSession().getAttribute("verify_code");
            if (code==null||"".equals(code)||!verify_code.toLowerCase().equals(code.toLowerCase())){
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                //RespBean.error("验证码错误"))是一个对象，RestController=controller+responsebody把对象转换成json字符串输出
                //ObjectMapper是jackson的李哥类，用来json和对象的相互转化
                out.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码错误")));
                out.flush();
                out.close();
                return;
            }else {
                filterChain.doFilter(req,resp);
            }
        }else {
            filterChain.doFilter(req,resp);
        }

    }
}
