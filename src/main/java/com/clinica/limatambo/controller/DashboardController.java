package com.clinica.limatambo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminPanel() {
        return "admin-dashboard";
    }

    @GetMapping("/medico/dashboard")
    public String medicoPanel() {
        return "medico-dashboard";
    }
}