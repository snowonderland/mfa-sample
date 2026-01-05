package com.example.mfa.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

/**
 * Description: 自定义OTT成功处理器，负责将令牌传递给最终用户。
 * 一般是将安全链接发送到邮箱，用户点击点击后完成二次认证。
 *
 * Created on 2026/1/4 11:01.
 */
@Slf4j
public class CustomOttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");

    @Override
    public void handle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull OneTimeToken oneTimeToken) throws IOException, ServletException {
        // 构建魔法链接
        String magicLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/ott-submit")
                .queryParam("token", oneTimeToken.getTokenValue())
                .toUriString();
        // 生成环境应该发送到用户邮箱或手机
        log.info("用户 {} 的验证链接: {}", oneTimeToken.getUsername(), magicLink);

        // 重定向到提示页面
        this.redirectHandler.handle(request, response, oneTimeToken);
    }
}
