package com.spring.project.models.users.userservices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.project.models.users.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verCode = ?1")
    public User findByVerCode(String verCode);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User retrieveUserByID(long userId);

    @Query("SELECT u FROM User u WHERE u.subscription = ?1")
    List<User> findBySubscription(boolean isSubscribed);

     
}
