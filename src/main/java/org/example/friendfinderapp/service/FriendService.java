// src/main/java/org/example/friendfinderapp/service/FriendService.java
package org.example.friendfinderapp.service;

import org.example.friendfinderapp.model.FriendRequest;
import org.example.friendfinderapp.model.FriendType;
import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.repository.FriendRequestRepository;
import org.example.friendfinderapp.repository.FriendTypeRepository;
import org.example.friendfinderapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendService {
    private final FriendRequestRepository reqRepo;
    private final FriendTypeRepository   typeRepo;
    private final UserRepository         userRepo;

    public FriendService(FriendRequestRepository reqRepo,
                         FriendTypeRepository typeRepo,
                         UserRepository userRepo) {
        this.reqRepo  = reqRepo;
        this.typeRepo = typeRepo;
        this.userRepo = userRepo;
    }

    /** Yeni arkadaşlık isteği gönderir */
    @Transactional
    public void sendFriendRequest(String requesterUsername,
                                  String targetUsername,
                                  Long typeId) {
        User requester = userRepo.findByUsername(requesterUsername);
        User target    = userRepo.findByUsername(targetUsername);
        FriendType type = typeRepo.findById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("FriendType bulunamadı: " + typeId));

        if (target.getBlockedUsers().contains(requester)) {
            return; // engellenmişse istek gönderme
        }

        FriendRequest fr = new FriendRequest();
        fr.setRequester(requester);
        fr.setTarget(target);
        fr.setType(type);
        fr.setAccepted(false);
        reqRepo.save(fr);
    }

    /** Gelen, henüz cevaplanmamış istekleri listeler */
    public List<FriendRequest> listIncomingRequests(String username) {
        return reqRepo.findByTarget_UsernameAndAcceptedFalse(username);
    }

    /** Gönderilen, henüz cevaplanmamış istekleri listeler */
    public List<FriendRequest> listOutgoingRequests(String username) {
        return reqRepo.findByRequester_UsernameAndAcceptedFalse(username);
    }

    /** İsteği kabul eder, iki kullanıcıyı arkadaş olarak birbirine ekler */
    @Transactional
    public void acceptFriendRequest(Long requestId) {
        FriendRequest fr = reqRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("İstek bulunamadı: " + requestId));
        fr.setAccepted(true);
        reqRepo.save(fr);

        User a = fr.getRequester();
        User b = fr.getTarget();
        a.getFriends().add(b);
        b.getFriends().add(a);
        userRepo.save(a);
        userRepo.save(b);
    }

    /** İsteği siler (reddeder) */
    @Transactional
    public void rejectFriendRequest(Long requestId) {
        reqRepo.deleteById(requestId);
    }
}
