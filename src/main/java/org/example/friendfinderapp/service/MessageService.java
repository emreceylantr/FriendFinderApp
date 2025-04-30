// src/main/java/org/example/friendfinderapp/service/MessageService.java
package org.example.friendfinderapp.service;

import org.example.friendfinderapp.model.Message;
import java.util.List;

public interface MessageService {
    List<Message> getConversation(String userA, String userB);
    void sendMessage(String fromUsername, String toUsername, String content);

    // ✅ Admin için eklenen metot
    List<Message> getMessagesOfUser(String username);
}