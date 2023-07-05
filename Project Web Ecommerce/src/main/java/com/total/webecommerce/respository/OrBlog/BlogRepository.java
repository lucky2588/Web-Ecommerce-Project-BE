package com.total.webecommerce.respository.OrBlog;

import com.total.webecommerce.entity.Blog;

import com.total.webecommerce.entity.projection.Public.BlogInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;
public interface BlogRepository extends JpaRepository<Blog, Integer> {
     @Query(value = " select b from Blog b where b.statusBlog =1")
    Page<BlogInfo> findBlogs(Pageable pageable);

    @Query(value = " select b from Blog b")
    Page<BlogInfo> findBlogsAdmin(Pageable pageable);
//    @Query(value = " select b from Blog b where b.user.id = ?2")
//    Page<BlogInfo> findBlogsAuthor(Pageable pageable,Integer userId);

    @Query("select b from Blog b where b.user.id = ?1")
    Page<BlogInfo> findBlogsAuthor(Integer id, Pageable pageable);


    @Query("select b from Blog b where b.brand.id = ?1")
    List<Blog> findByBrand_Id(Integer id);





     @Query(value="select b from Blog b where b.id = ?1")
     Optional<BlogInfo> findBlogbyId(Integer id);


     @Query(nativeQuery = true , value = "select * from Blog b where b.status_blog = 1 ORDER BY b.view_blog desc limit 4")
     List<Blog> getBlogsHaveViewTop();
@Query( nativeQuery = true , value = "select *from Blog b where b.brand_id = ?1 and b.status_blog = 1 and b.id != ?2 limit 2")
    List<Blog> findBrand(Integer brandId,Integer blogId);
}