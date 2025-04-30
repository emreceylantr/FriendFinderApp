// src/main/java/org/example/friendfinderapp/controller/FriendController.java
package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FriendController {

    private final UserService userService;

    @Autowired
    public FriendController(UserService userService) {
        this.userService = userService;
    }

    // Yeni arkadaşlık isteği gönderme
    @PostMapping("/friends/add")
    public String sendFriendRequest(
            @RequestParam("username") String targetUsername,
            @RequestParam("typeId") Long typeId,
            RedirectAttributes redirectAttrs) {
        // TODO: Oturum açmış kullanıcı adını SecurityContext ya da Session'dan alın
        String requesterUsername = "emre";
        try {
            userService.sendFriendRequest(requesterUsername, targetUsername, typeId);
            redirectAttrs.addFlashAttribute("msg", "İstek gönderildi: " + targetUsername);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "İstek gönderilemedi: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    // Gelen isteğe yanıt verme (kabul / reddet)
    @PostMapping("/friends/requests/respond")
    public String respondFriendRequest(
            @RequestParam("requestId") Long requestId,
            @RequestParam("accept") boolean accept,
            RedirectAttributes redirectAttrs) {
        try {
            if (accept) {
                userService.acceptFriendRequest(requestId);
                redirectAttrs.addFlashAttribute("msg", "İstek kabul edildi.");
            } else {
                userService.rejectFriendRequest(requestId);
                redirectAttrs.addFlashAttribute("msg", "İstek reddedildi.");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "İşlem başarısız oldu: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}
