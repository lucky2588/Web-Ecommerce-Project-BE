package com.total.webecommerce.respository.OfAdmin;

import com.total.webecommerce.entity.Category;
import com.total.webecommerce.entity.dto.CategoryWeb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    @Query("select c from Category c where c.id = ?1")
    Optional<CategoryWeb> findByCategoryWeb(Integer id);







}