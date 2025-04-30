package org.example.friendfinderapp.service;

import org.example.friendfinderapp.model.FriendRequest;
import org.example.friendfinderapp.model.FriendType;
import org.example.friendfinderapp.model.Role;
import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.repository.FriendRequestRepository;
import org.example.friendfinderapp.repository.FriendTypeRepository;
import org.example.friendfinderapp.repository.MessageRepository;
import org.example.friendfinderapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final FriendTypeRepository friendTypeRepo;
    private final FriendRequestRepository friendRequestRepo;
    private final MessageRepository messageRepo;

    public UserService(UserRepository userRepo,
                       FriendTypeRepository friendTypeRepo,
                       FriendRequestRepository friendRequestRepo,
                       MessageRepository messageRepo) {
        this.userRepo = userRepo;
        this.friendTypeRepo = friendTypeRepo;
        this.friendRequestRepo = friendRequestRepo;
        this.messageRepo = messageRepo;
    }

    public User register(User user) {
        user.setRole(Role.USER);
        return userRepo.save(user);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> authenticate(String username, String password) {
        User u = userRepo.findByUsername(username);
        if (u != null && u.getPassword().equals(password)) {
            return Optional.of(u);
        }
        return Optional.empty();
    }

    public List<User> findAllExcept(Long id) {
        return userRepo.findAllExcept(id);
    }

    public List<FriendType> listFriendTypes(Long userId) {
        List<FriendType> friendTypes = new ArrayList<>();
        friendTypeRepo.findAll().forEach(friendTypes::add);
        return friendTypes;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public List<User> getFriends(String username) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.getFriends() != null) {
            return new ArrayList<>(user.getFriends());
        }
        return Collections.emptyList();
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = findById(userId);

        // Kullanıcıyı tüm arkadaş listelerinden çıkar
        List<User> allUsers = userRepo.findAll();
        for (User other : allUsers) {
            other.getFriends().remove(user);
        }

        // Arkadaşlık isteklerini sil
        friendRequestRepo.deleteAllByRequester_IdOrTarget_Id(userId, userId);

        // Mesajları sil
        messageRepo.deleteAllBySender_IdOrReceiver_Id(userId, userId);

        // Kullanıcıyı sil
        userRepo.deleteById(userId);
    }

    public void sendFriendRequest(String requesterUsername, String targetUsername, Long typeId) {
        User requester = userRepo.findByUsername(requesterUsername);
        User target = userRepo.findByUsername(targetUsername);
        FriendType type = friendTypeRepo.findById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("FriendType bulunamadı: " + typeId));

        if (requester == null || target == null) {
            throw new IllegalArgumentException("Geçersiz kullanıcı adı.");
        }

        FriendRequest request = new FriendRequest();
        request.setRequester(requester);
        request.setTarget(target);
        request.setType(type);
        request.setAccepted(false);
        friendRequestRepo.save(request);
    }

    public void acceptFriendRequest(Long requestId) {
        FriendRequest req = friendRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("İstek bulunamadı: " + requestId));

        User fromUser = req.getRequester();
        User toUser = req.getTarget();

        fromUser.getFriends().add(toUser);
        toUser.getFriends().add(fromUser);
        userRepo.save(fromUser);
        userRepo.save(toUser);

        friendRequestRepo.delete(req);
    }

    public void rejectFriendRequest(Long requestId) {
        friendRequestRepo.deleteById(requestId);
    }

    public int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String calculateZodiac(LocalDate dateOfBirth) {
        int m = dateOfBirth.getMonthValue(), day = dateOfBirth.getDayOfMonth();
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

    @Transactional
    public void removeFriend(String currentUsername, String friendUsername) {
        User currentUser = userRepo.findByUsername(currentUsername);
        User friend = userRepo.findByUsername(friendUsername);
        if (currentUser != null && friend != null) {
            currentUser.getFriends().remove(friend);
            friend.getFriends().remove(currentUser);
            userRepo.save(currentUser);
            userRepo.save(friend);
        }
    }
}
