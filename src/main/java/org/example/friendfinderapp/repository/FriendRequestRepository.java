package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.FriendRequest;
import org.example.friendfinderapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Hedef kullanıcıya gelen ve kabul edilmemiş istekler
    @Query("SELECT fr FROM FriendRequest fr " +
            "JOIN FETCH fr.requester " +
            "JOIN FETCH fr.type " +
            "WHERE fr.target.username = :username AND fr.accepted = false")
    List<FriendRequest> findIncomingWithDetails(@Param("username") String username);

    // Gönderenin yolladığı ve henüz kabul edilmemiş istekler
    @Query("SELECT fr FROM FriendRequest fr " +
            "JOIN FETCH fr.target " +
            "JOIN FETCH fr.type " +
            "WHERE fr.requester.username = :username AND fr.accepted = false")
    List<FriendRequest> findOutgoingWithDetails(@Param("username") String username);

    // ✅ Kullanıcı silinmeden önce ilgili tüm istekleri sil
    void deleteAllByRequester_IdOrTarget_Id(Long requesterId, Long targetId);
}
