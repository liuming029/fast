package com.fast.system.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fast.system.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证失败处理
 *
 * @author fast
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(AjaxResult.error(401, "登录已过期，请重新登录。")));
        out.flush();
        out.close();
    }
}