package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Hedef kullanıcıya gelen ve kabul edilmemiş istekler
    List<FriendRequest> findByTarget_UsernameAndAcceptedFalse(String username);

    // Gönderenin yolladığı ve henüz kabul edilmemiş istekler
    List<FriendRequest> findByRequester_UsernameAndAcceptedFalse(String username);

    // ✅ Kullanıcı silinmeden önce ilgili tüm istekleri sil
    void deleteAllByRequester_IdOrTarget_Id(Long requesterId, Long targetId);
}
