package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.model.Role;
import org.example.friendfinderapp.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/profile";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Bu kullanıcı adı zaten kayıtlı");
            return "register";
        }

        // ✅ Enum kullanımı: USER rolünü ata
        user.setRole(Role.USER);

        try {
            userService.register(user);
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("error", "Veritabanı hatası, lütfen tekrar deneyin");
            return "register";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Beklenmedik bir hata oluştu.");
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        if (logout != null) {
            model.addAttribute("logout", true);
        }
        return "login";
    }
}
