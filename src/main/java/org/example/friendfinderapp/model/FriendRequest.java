package org.example.friendfinderapp.model;

import jakarta.persistence.*;

@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // İsteği gönderen kullanıcı
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // İsteğin hedefi olan kullanıcı
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    // İsteğin tipi (Arkadaş, Dost, vb.)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private FriendType type;

    // Kabul durumu (true → kabul edildi, false → beklemede)
    @Column(nullable = false)
    private boolean accepted = false;

    // --- Getter & Setter'lar ---

    public Long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public FriendType getType() {
        return type;
    }

    public void setType(FriendType type) {
        this.type = type;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
