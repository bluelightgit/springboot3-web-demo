package com.mySpring.demo.CORSFilter;

import java.io.IOException;

import org.springframework.core.annotation.Order;

import jakarta.servlet.*;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@Order(1)
@WebFilter(filterName = "corsFilter", urlPatterns = {"/*"})
public class CORSFilter implements Filter {
     
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init filter...");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("filter is working...");
    }
    @Override
    public void destroy() {
        System.out.println("destroy filter...");
    }
}
