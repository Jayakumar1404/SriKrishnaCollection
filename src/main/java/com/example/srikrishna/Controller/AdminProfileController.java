package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminProfileController {

    private boolean authenticated(HttpSession session) {
        return session.getAttribute("loggedInAdmin") != null;
    }

    @GetMapping("/admin/profile")
    public String profile(HttpSession session, Model model) {
        if (!authenticated(session)) return "redirect:/admin/login";
        model.addAttribute("adminUsername", session.getAttribute("loggedInAdmin"));
        return "admin/profile";
    }

    @GetMapping("/admin/settings")
    public String settings(HttpSession session, Model model) {
        if (!authenticated(session)) return "redirect:/admin/login";
        model.addAttribute("adminUsername", session.getAttribute("loggedInAdmin"));
        return "admin/settings";
    }
}
