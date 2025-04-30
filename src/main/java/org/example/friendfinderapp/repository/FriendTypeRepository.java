// src/main/java/org/example/friendfinderapp/repository/FriendTypeRepository.java
package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.FriendType;
import org.springframework.data.repository.CrudRepository;

public interface FriendTypeRepository extends CrudRepository<FriendType, Long> {
    // Spring Data JPA findById() metodunu zaten sağlar
    // Gerekli ise, ek metodlar ekleyebilirsiniz
}
