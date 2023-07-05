package com.total.webecommerce.respository.OfUser;

import com.total.webecommerce.entity.User;
import com.total.webecommerce.entity.projection.OfUser.ManagentUser;
import com.total.webecommerce.entity.projection.OfUser.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);

    @Query("select u from User u where u.id = ?1")
    Optional<ManagentUser> findUser(Integer id);


    @Query("select u from User u where u.email = ?1")
    Optional<UserInfo> findEmailWithInfo(String email);
    @Query("select u from User u where u.id = ?1")
    Optional<UserInfo> findUserlWithId(Integer id);
    List<UserInfo> findByPhone(String phone);

    @Query("select u from User u inner join u.roles roles where roles.name = ?1")
    List<User> findByRoles_Name(String roleName);

    @Query("select u from User u where u.email = ?1")
    User findEmail(String email);

    @Query("select u from User u where u.email = ?1 and u.id != ?2")
    Optional<User> confirmEmailAndId(String email , Integer id);

    @Query("select u from User u join u.roles roles where roles.id = 2 or roles.id = 3")
    Page<UserInfo> findAllWithUserInfo(Pageable pageable);







    Page<User> findAll(Pageable pageable);

}