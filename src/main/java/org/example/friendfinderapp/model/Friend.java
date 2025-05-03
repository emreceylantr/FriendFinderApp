package org.example.friendfinderapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private FriendType type;

    public Friend() {}

    public Friend(Long id, User user, User friend, FriendType type) {
        this.id = id;
        this.user = user;
        this.friend = friend;
        this.type = type;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public User getFriend() { return friend; }
    public FriendType getType() { return type; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setFriend(User friend) { this.friend = friend; }
    public void setType(FriendType type) { this.type = type; }
}
