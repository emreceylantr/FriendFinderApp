package org.example.friendfinderapp.repository;

import org.example.friendfinderapp.model.User;
import org.example.friendfinderapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    // Tüm user’lar arasından kendisi hariç olanları listelemek için:
    @Query("SELECT u FROM User u WHERE u.id <> :id")
    List<User> findAllExcept(@Param("id") Long id);

    // id’si verilen user’ın arkadaşlarını getirmek için:
    @Query("SELECT f FROM User u JOIN u.friends f WHERE u.id = :userId")
    List<User> findFriendsByUserId(@Param("userId") Long userId);

    // Belirli role sahip kullanıcıları listelemek için (ör: MODERATOR olanlar)
    List<User> findByRole(Role role);
}
