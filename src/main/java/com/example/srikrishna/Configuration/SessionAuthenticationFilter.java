package com.example.srikrishna.Configuration;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        String contextPath = request.getContextPath();

        String path = uri.substring(contextPath.length());

        HttpSession session = request.getSession(false);

        // =========================================================
        // CHECK CUSTOMER LOGIN
        // =========================================================

        boolean customerLoggedIn = false;

        if (session != null) {

            customerLoggedIn =
                    session.getAttribute("loggedInCustomer") != null;
        }

        // =========================================================
        // CUSTOMER PUBLIC PAGES
        // =========================================================

        boolean customerPublicPage =
                path.equals("/login")
                || path.equals("/register")
                || path.equals("/forgot-password")
                || path.equals("/verify-otp")
                || path.equals("/reset-password")
                || path.equals("/logout");

        // =========================================================
        // CUSTOMER PROTECTED AREA
        // =========================================================

        boolean customerProtected =
                path.equals("/Krishna")
                || path.startsWith("/Krishna/");

        if (customerProtected
                && !customerPublicPage
                && !customerLoggedIn) {

            response.sendRedirect(
                    contextPath + "/login");

            return;
        }

        // =========================================================
        // CHECK ADMIN LOGIN
        // =========================================================

        boolean adminLoggedIn = false;

        if (session != null) {

            adminLoggedIn =
                    session.getAttribute("loggedInAdmin") != null;
        }

        // =========================================================
        // ADMIN LOGIN PAGE
        // =========================================================

        boolean adminLoginPage =
                path.equals("/admin/login");

        // =========================================================
        // ADMIN PROTECTED AREA
        // =========================================================

        boolean adminProtected =
                path.equals("/admin")
                || path.startsWith("/admin/");

        if (adminProtected
                && !adminLoginPage
                && !adminLoggedIn) {

            response.sendRedirect(
                    contextPath + "/admin/login");

            return;
        }

        // =========================================================
        // CONTINUE REQUEST
        // =========================================================

        filterChain.doFilter(request, response);
    }
}