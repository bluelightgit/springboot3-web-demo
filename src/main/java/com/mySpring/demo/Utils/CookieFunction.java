package com.mySpring.demo.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieFunction {
    // 设置cookie
    public void setCookie(HttpServletResponse response, String uuid) {
        Cookie cookie = new Cookie("visitorUUID", uuid);
        cookie.setMaxAge(Integer.MAX_VALUE); // 设置cookie的有效期为最大值
        response.addCookie(cookie);
    }

    // 获取cookie
    public String getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visitorUUID")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
