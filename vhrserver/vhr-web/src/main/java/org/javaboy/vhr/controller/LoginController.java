package org.javaboy.vhr.controller;

import org.javaboy.vhr.config.VerificationCode;
import org.javaboy.vhr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login(){
        return RespBean.error("尚未登陆，请登录");
    }

    @GetMapping("/verify")
    public void verification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = req.getSession();
        session.setAttribute("verify_code",text);
        //以流的方式将验证码发送到前端
        VerificationCode.output(image,resp.getOutputStream());
    }
}
