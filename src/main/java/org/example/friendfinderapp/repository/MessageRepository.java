package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
      select m from Message m 
      where (m.sender.username = :u1 and m.receiver.username = :u2)
         or (m.sender.username = :u2 and m.receiver.username = :u1)
      order by m.sentAt
      """)
    List<Message> findConversation(String u1, String u2);

    List<Message> findBySenderUsernameOrReceiverUsername(String sender, String receiver);

    // ✅ Eksik olan bu: sender_id veya receiver_id eşleşen tüm mesajları sil
    void deleteAllBySender_IdOrReceiver_Id(Long senderId, Long receiverId);
}
