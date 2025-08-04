package com.snowball.snowball.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing a friendship relationship between two users.
 * <p>
 * The 'status' field indicates the state of the friend request, such as "REQUESTED" or "ACCEPTED".
 * The 'direction' field indicates the direction of the friend request relative to the user,
 * such as "OUTGOING" for requests sent by the user, or "INCOMING" for requests received by the user.
 * </p>
 */
@Entity
@Table(name = "user_friends", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who initiated the friend request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // User who received the friend request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    @JsonIgnore
    private User friend;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "REQUESTED"; // Possible values: "REQUESTED", "ACCEPTED", etc.

    /**
     * Direction of the friend request relative to the user.
     * Possible values: "OUTGOING" (request sent by user), "INCOMING" (request received by user), etc.
     */
    @Column(length = 20)
    @Builder.Default
    private String direction = "OUTGOING";
}