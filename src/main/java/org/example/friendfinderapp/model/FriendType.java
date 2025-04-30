package org.example.friendfinderapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="friend_types")
@Getter @Setter @NoArgsConstructor
public class FriendType {
    @Id @GeneratedValue
    private Integer id;
    @Column(nullable=false) private String typeName;
}
