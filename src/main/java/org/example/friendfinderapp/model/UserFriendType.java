package org.example.friendfinderapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="user_friend_types")
@Getter @Setter @NoArgsConstructor
public class UserFriendType {
    @Id @GeneratedValue private Long id;
    @ManyToOne(optional=false) private User user;
    @ManyToOne(optional=false) private User friend;
    @ManyToOne(optional=false) private FriendType type;
}
