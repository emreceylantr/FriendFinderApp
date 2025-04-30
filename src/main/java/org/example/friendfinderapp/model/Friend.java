// src/main/java/org/example/friendfinderapp/model/Friend.java
package org.example.friendfinderapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "friends")     // tablo adını burada tam olarak veriyoruz
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @JoinColumn(name = "friend_id")
    private User friend;

    public Friend() {}
    public Friend(Long id, User user, User friend) {
        this.id = id;
        this.user = user;
        this.friend = friend;
    }

    // getter & setter’lar
    public Long getId() { return id; }
    public User getUser() { return user; }
    public User getFriend() { return friend; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setFriend(User friend) { this.friend = friend; }
}
