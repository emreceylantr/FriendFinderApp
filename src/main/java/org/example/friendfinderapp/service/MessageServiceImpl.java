package org.example.friendfinderapp.service;

import org.example.friendfinderapp.model.Message;
import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.repository.MessageRepository;
import org.example.friendfinderapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository msgRepo;
    private final UserRepository userRepo;

    public MessageServiceImpl(MessageRepository msgRepo, UserRepository userRepo) {
        this.msgRepo = msgRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<Message> getConversation(String userA, String userB) {
        return msgRepo.findConversation(userA, userB);
    }

    @Override
    @Transactional
    public void sendMessage(String fromUsername, String toUsername, String content) {
        User from = userRepo.findByUsername(fromUsername);
        User to = userRepo.findByUsername(toUsername);
        Message m = new Message();
        m.setSender(from);
        m.setReceiver(to);
        m.setContent(content);
        msgRepo.save(m);
    }

    @Override
    public List<Message> getMessagesOfUser(String username) {
        return msgRepo.findBySenderUsernameOrReceiverUsername(username, username);
    }
}
