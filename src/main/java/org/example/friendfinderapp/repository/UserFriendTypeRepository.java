// src/main/java/org/example/friendfinderapp/repository/UserFriendTypeRepository.java
package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.UserFriendType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFriendTypeRepository extends JpaRepository<UserFriendType, Long> { }
