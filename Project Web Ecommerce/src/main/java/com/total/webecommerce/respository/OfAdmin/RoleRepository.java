package com.total.webecommerce.respository.OfAdmin;

import com.total.webecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);




}