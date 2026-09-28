package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/report")
public class ReportController {

    @GetMapping
    public String reportPage() {

        return "admin/reports";

    }

}