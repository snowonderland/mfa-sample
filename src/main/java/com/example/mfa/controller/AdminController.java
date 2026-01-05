package com.example.mfa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 管理员
 *
 * @author libinbin
 * Created in 2026/1/5 9:43.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping(name = "用户管理", path = "/users")
    public String users() {
        return "users";
    }

    @GetMapping(name = "系统设置", path = "/settings")
    public String settings() {
        return "settings";
    }

    @GetMapping(name = "日志管理", path = "/logs")
    public String logs() {
        return "logs";
    }
}
