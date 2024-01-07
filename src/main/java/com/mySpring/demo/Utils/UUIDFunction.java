package com.mySpring.demo.Utils;

import jakarta.servlet.http.Cookie;

public class UUIDFunction {
    public String setUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public String getUUIDFromCookie(Cookie[] cookie) {
        if (cookie != null) {
            for (Cookie c : cookie) {
                if (c.getName().equals("visitorUUID")) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

}
