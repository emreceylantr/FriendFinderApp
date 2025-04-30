// src/main/java/org/example/friendfinderapp/controller/ModeratorController.java
package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.model.Role;
import org.example.friendfinderapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    private final UserService userService;

    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Admin olmayan tüm kullanıcıları listeler
    @GetMapping("/users")
    public String listUsers(Model model, Principal principal) {
        List<User> users = userService.getAllUsers();

        // Admin ve kendisi hariç
        List<User> visibleUsers = users.stream()
                .filter(u -> !u.getUsername().equalsIgnoreCase(principal.getName()))
                .filter(u -> u.getRole() != Role.ADMIN)
                .collect(Collectors.toList());

        model.addAttribute("users", visibleUsers);
        return "moderator/users";  // HTML şablonun bu dosya olmalı
    }

    // ✅ Kullanıcıyı sil
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/moderator/users";
    }

    // ✅ Kullanıcı profilini görüntüle
    @GetMapping("/profile/{username}")
    public String viewProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "view-profile";  // Ortak profil görüntüleme sayfasını kullanır
    }
}
