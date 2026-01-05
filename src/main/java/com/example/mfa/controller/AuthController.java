package com.example.mfa.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面
 */
@Controller
public class AuthController {

    /**
     * 登录页面
     *
     * @return 登录页面
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        // 获取登录失败时的用户名
        String username = request.getParameter("username");
        if (username == null) {
            username = (String) request.getSession().getAttribute("SPRING_SECURITY_LAST_USERNAME");
        }

        // 将用户名添加到模型中，用于在登录失败时回填
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "login";
    }

    /**
     * 获取令牌页面
     *
     * @return 页面
     */
    @GetMapping("/login-ott")
    public String loginOtt(HttpServletRequest request, Model model) {
        // 获取登录失败时的用户名
        String username = request.getParameter("username");
        if (username == null) {
            username = (String) request.getSession().getAttribute("SPRING_SECURITY_LAST_USERNAME");
        }

        // 将用户名添加到模型中，用于在登录失败时回填
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "login-ott";
    }

    /**
     * 提交令牌页面
     *
     * @return 页面
     */
    @GetMapping("/ott-submit")
    public String otpPage(HttpServletRequest request, Model model) {
        // 获取登录失败时的用户名
        String token = request.getParameter("token");
        // 将用户名添加到模型中，用于在登录失败时回填
        if (token != null) {
            model.addAttribute("tokenValue", token);
        }
        return "ott-submit";
    }

    @GetMapping("/ott/sent")
    public String ottSend() {
        return "ott-send";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}