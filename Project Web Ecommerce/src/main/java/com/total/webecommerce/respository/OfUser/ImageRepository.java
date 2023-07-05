package com.total.webecommerce.respository.OfUser;

import com.total.webecommerce.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByUser_Id(Integer id);

    Image findByUser_IdAndId(Integer id, Integer id1);
    
    


}