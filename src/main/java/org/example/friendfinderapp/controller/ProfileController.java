package org.example.friendfinderapp.controller;

import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.service.FriendService;
import org.example.friendfinderapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FriendService friendService;

    public ProfileController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        User me = userService.findByUsername(principal.getName());
        model.addAttribute("user", me);

        if (me.getDateOfBirth() != null) {
            int age = Period.between(me.getDateOfBirth(), LocalDate.now()).getYears();
            model.addAttribute("age", age);
            model.addAttribute("zodiac", calculateZodiac(me.getDateOfBirth()));
        }

        model.addAttribute("friends", me.getFriends());
        model.addAttribute("incomingRequests", friendService.listIncomingRequests(me.getUsername()));
        model.addAttribute("outgoingRequests", friendService.listOutgoingRequests(me.getUsername()));
        model.addAttribute("allUsers", userService.findAllExcept(me.getId()));
        model.addAttribute("friendTypes", userService.listFriendTypes(me.getId()));
        model.addAttribute("gallery", me.getGalleryPhotos());

        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") User form, Principal principal) {
        User me = userService.findByUsername(principal.getName());
        me.setFirstName(form.getFirstName());
        me.setLastName(form.getLastName());
        me.setDateOfBirth(form.getDateOfBirth());
        me.setPhotoUrl(form.getPhotoUrl());
        me.setBio(form.getBio());
        userService.save(me);
        return "redirect:/profile";
    }

    @PostMapping("/friends/add")
    public String sendRequest(@RequestParam String username,
                              @RequestParam Long typeId,
                              Principal principal) {
        friendService.sendFriendRequest(principal.getName(), username, typeId);
        return "redirect:/profile";
    }

    @PostMapping("/friends/accept")
    public String acceptRequest(@RequestParam Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return "redirect:/profile";
    }

    @PostMapping("/friends/reject")
    public String rejectRequest(@RequestParam Long requestId) {
        friendService.rejectFriendRequest(requestId);
        return "redirect:/profile";
    }

    @PostMapping("/friends/remove")
    public String removeFriend(@RequestParam String username, Principal principal) {
        userService.removeFriend(principal.getName(), username);
        return "redirect:/profile";
    }

    @PostMapping("/block/{username}")
    public String blockUser(@PathVariable String username, Principal principal) {
        userService.blockUser(principal.getName(), username);
        return "redirect:/profile";
    }

    @PostMapping("/unblock/{username}")
    public String unblockUser(@PathVariable String username, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        User toUnblock = userService.findByUsername(username);
        if (currentUser != null && toUnblock != null) {
            currentUser.getBlockedUsers().remove(toUnblock);
            userService.save(currentUser);
        }
        return "redirect:/profile";
    }

    @PostMapping("/gallery/add")
    public String addGalleryPhoto(@RequestParam("url") String photoUrl, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (photoUrl != null && !photoUrl.isBlank()) {
            user.getGalleryPhotos().add(photoUrl);
            userService.save(user);
        }
        return "redirect:/profile";
    }

    @PostMapping("/gallery/delete")
    public String deleteGalleryPhoto(@RequestParam("url") String photoUrl, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (photoUrl != null && user.getGalleryPhotos().contains(photoUrl)) {
            user.getGalleryPhotos().remove(photoUrl);
            userService.save(user);
        }
        return "redirect:/profile";
    }

    @GetMapping("/view/{username}")
    public String viewOtherProfile(@PathVariable String username, Model model, Principal principal) {
        User viewedUser = userService.findByUsername(username);
        if (viewedUser == null) {
            return "redirect:/profile";
        }

        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", viewedUser);
        model.addAttribute("friends", viewedUser.getFriends());

        if (viewedUser.getDateOfBirth() != null) {
            int age = Period.between(viewedUser.getDateOfBirth(), LocalDate.now()).getYears();
            model.addAttribute("age", age);
            model.addAttribute("zodiac", calculateZodiac(viewedUser.getDateOfBirth()));
        }

        model.addAttribute("isFriend", currentUser.getFriends().contains(viewedUser));
        model.addAttribute("friendTypes", userService.listFriendTypes(currentUser.getId()));
        model.addAttribute("gallery", viewedUser.getGalleryPhotos());

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
