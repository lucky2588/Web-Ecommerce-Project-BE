package com.total.webecommerce.respository.OfUser;

import com.total.webecommerce.entity.Favorites;
import com.total.webecommerce.entity.projection.OfUser.FavoritesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
    @Query("select f from Favorites f where f.user.id = ?1 and f.status = true")
    FavoritesInfo findByUser_IdAndStatusTrue(Integer id);


    @Query("select f from Favorites f where f.user.email = ?1")
    Favorites findByUser_Email(String email);

    @Query("select f from Favorites f inner join f.products products where f.user.id = ?1 and products.id not in ?2")
    List<FavoritesInfo> findByUser_IdAndProducts_IdNotIn(Integer id, Collection<Integer> ids);















}