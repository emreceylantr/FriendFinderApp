// src/main/java/org/example/friendfinderapp/controller/ChatController.java
package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.model.Message;
import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.service.MessageService;
import org.example.friendfinderapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final UserService    userService;
    private final MessageService messageService;

    public ChatController(UserService userService,
                          MessageService messageService) {
        this.userService    = userService;
        this.messageService = messageService;
    }

    @GetMapping("/{username}")
    public String chatWith(@PathVariable String username,
                           Principal principal,
                           Model model) {
        User me    = userService.findByUsername(principal.getName());
        User other = userService.findByUsername(username);

        List<Message> history = messageService.getConversation(me.getUsername(), other.getUsername());

        model.addAttribute("me", me);
        model.addAttribute("other", other);
        model.addAttribute("history", history);
        return "chat";
    }

    @PostMapping("/{username}")
    public String sendMessage(@PathVariable String username,
                              @RequestParam String content,
                              Principal principal) {
        messageService.sendMessage(principal.getName(), username, content);
        return "redirect:/chat/" + username;
    }
}
