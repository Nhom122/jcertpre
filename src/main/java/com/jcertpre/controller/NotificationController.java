package com.jcertpre.controller;

import com.jcertpre.model.Notification;
import com.jcertpre.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/notifications")
    public String showAll(Model model) {
        List<Notification> notifications = service.getAll();
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @GetMapping("/notifications/add")
    public String showAddForm(Model model) {
        model.addAttribute("notification", new Notification());
        return "notification_form";
    }

    @PostMapping("/notifications/save")
    public String saveNotification(@ModelAttribute Notification notification) {
        service.save(notification);
        return "redirect:/notifications";
    }

    @GetMapping("/notifications/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Notification notification = service.getById(id);
        model.addAttribute("notification", notification);
        return "notification_form";
    }

    @GetMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/notifications";
    }
}
