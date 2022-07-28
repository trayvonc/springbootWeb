package com.fan.utils;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Component
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里进行验证码的校验
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        // 取出验证码
        String validateCode = (String) session.getAttribute("validateCode");
        // 取出页面的验证码
        // 输入的验证和session中的验证进行对比
        String randomcode = httpServletRequest.getParameter("randomcode");
        System.out.println(validateCode+","+randomcode);
        if (randomcode != null && validateCode != null && !randomcode.equals(validateCode)) {
            System.out.println("验证码错误");
            // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
            httpServletRequest.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");//自定义登录异常
            // 拒绝访问，不再校验账号和密码
            return true;
        }
        return super.onAccessDenied(request, response);
    }
}
