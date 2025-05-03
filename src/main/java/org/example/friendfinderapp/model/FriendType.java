package org.example.friendfinderapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_types")
@Getter
@Setter
@NoArgsConstructor
public class FriendType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false) // ❗ Veritabanındaki sütun adı "name"
    private String name;
}
