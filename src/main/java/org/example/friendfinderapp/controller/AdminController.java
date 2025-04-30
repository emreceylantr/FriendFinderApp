// src/main/java/org/example/friendfinderapp/controller/AdminController.java
package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.model.Message;
import org.example.friendfinderapp.model.Role;
import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.service.MessageService;
import org.example.friendfinderapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MessageService messageService;

    public AdminController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/makeModerator/{id}")
    public String makeModerator(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setRole(Role.MODERATOR);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/messages/{username}")
    public String viewMessages(@PathVariable String username, Model model) {
        List<Message> allMessages = messageService.getMessagesOfUser(username);

        Map<String, List<Message>> grouped = allMessages.stream()
                .filter(m -> m.getSender().getUsername() != null && m.getReceiver().getUsername() != null)
                .collect(Collectors.groupingBy(m -> {
                    if (m.getSender().getUsername().equals(username)) {
                        return m.getReceiver().getUsername();
                    } else {
                        return m.getSender().getUsername();
                    }
                }));

        model.addAttribute("username", username);
        model.addAttribute("conversations", grouped);
        return "admin/messages";
    }

    @GetMapping("/profile/{username}")
    public String viewProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        // Doğum tarihi varsa yaş ve burç hesapla
        if (user.getDateOfBirth() != null) {
            int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();
            model.addAttribute("age", age);
            model.addAttribute("zodiac", calculateZodiac(user.getDateOfBirth()));
        }

        model.addAttribute("friends", userService.getFriends(username));
        return "view-profile";
    }

    private String calculateZodiac(LocalDate d) {
        int m = d.getMonthValue(), day = d.getDayOfMonth();
        return switch (m) {
            case 1 -> day < 20 ? "Oğlak" : "Kova";
            case 2 -> day < 19 ? "Kova" : "Balık";
            case 3 -> day < 21 ? "Balık" : "Koç";
            case 4 -> day < 20 ? "Koç" : "Boğa";
            case 5 -> day < 21 ? "Boğa" : "İkizler";
            case 6 -> day < 21 ? "İkizler" : "Yengeç";
            case 7 -> day < 23 ? "Yengeç" : "Aslan";
            case 8 -> day < 23 ? "Aslan" : "Başak";
            case 9 -> day < 23 ? "Başak" : "Terazi";
            case 10 -> day < 23 ? "Terazi" : "Akrep";
            case 11 -> day < 22 ? "Akrep" : "Yay";
            default -> day < 22 ? "Yay" : "Oğlak";
        };
    }
}
