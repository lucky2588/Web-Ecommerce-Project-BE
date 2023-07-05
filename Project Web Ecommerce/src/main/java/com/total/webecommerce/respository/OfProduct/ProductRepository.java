package com.total.webecommerce.respository.OfProduct;

import com.total.webecommerce.entity.Product;
import com.total.webecommerce.entity.projection.OfUser.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);

    @Query("select p from Product p where p.id = ?1")
    Optional<ProductInfo> FindProductById(Integer id);

    @Query("select p from Product p where p.brand.id = ?1")
    List<Product> removeBrand(Integer id);


    @Query(nativeQuery = true, value = "SELECT * FROM product a  WHERE a.status = 1 ORDER BY a.nums_sold DESC",
            countQuery = "SELECT count(*) FROM product a  WHERE a.status = 1 ORDER BY a.nums_sold DESC "
    )
    Page<ProductInfo> getProductOrderByNumsSoldOut(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM product a  WHERE a.status = 1 ORDER BY a.view DESC",
            countQuery = "SELECT count(*) FROM product a  WHERE a.status = 1 ORDER BY a.view DESC "
    )
    Page<ProductInfo> getProductOrderTopView(Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM product a  WHERE a.sales < 1 ORDER BY a.id DESC",
            countQuery = "SELECT count(*) FROM product a  WHERE a.status = 1 ORDER BY a.view DESC "
    )
    Page<ProductInfo> getProductNew(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT a.* , b.id AS discount_id FROM product a  join discount b on a.discount_id = b.id WHERE a.status = 1 ORDER BY a.view DESC",
            countQuery = "SELECT count(*) , b.id AS discount_id FROM product a left join discount b on a.discount_id = b.id  WHERE a.status = 1 ORDER BY a.view DESC "
    )
    Page<ProductInfo> ProductDiscout(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM product a  WHERE a.status = 1 ORDER BY a.view DESC")
    List<ProductInfo> getProductTop4BestView();

    @Query(nativeQuery = true, value = "SELECT * FROM product a  WHERE a.status = 1 ORDER BY a.nums_sold DESC")
    List<ProductInfo> getProductTop4BestSeller();


    long countByBrand_Id(Integer id);

    long countByCategory_Id(Integer id);

    List<ProductInfo> findByBrand_IdAndIdNotIn(Integer idBrand, Collection<Integer> ids);
    List<ProductInfo> findByBrand_Id(Integer id);
    List<Product> findByCategory_Id(Integer id);
    @Query(nativeQuery = true,
            value = "select * from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.price asc",
            countQuery = "select count(*) from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)"+
                    "\"order by a.price asc\"")
    Page<ProductInfo> getProductFilter(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("price") Long price, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.view asc",
            countQuery = "select count(*) from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)"+
                    "\"order by a.view asc\"")
    Page<ProductInfo> getProductFilterHaveViewHigh(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("price") Long price, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.nums_sold asc",
            countQuery = "select count(*) from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)"+
                    "\"order by a.nums_sold desc\"")
    Page<ProductInfo> getProductFilterHaveNumsSold(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("price") Long price, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.create_at desc",
            countQuery = "select count(*) from product a where (:brandId is null or a.brand_id = :brandId) and" +
                    "(:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)"+
                    "\"order by a.create_at desc\"")
    Page<ProductInfo> getProductFilterHaveCreateAt(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("price") Long price, Pageable pageable);



    @Query(nativeQuery = true,
            value = "select * from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.price asc",
            countQuery = "select count(*) from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.price asc")
    Page<ProductInfo> getProductFilterAboutCategory(
            @Param("categoryId") Integer categoryId,
            @Param("price") Long price, Pageable pageable
    );
    @Query(nativeQuery = true,
            value = "select * from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.view desc",
            countQuery = "select count(*) from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.view desc")
    Page<ProductInfo> getProductFilterAboutCategoryHaveViewHigh(Integer categoryId, Long price, PageRequest of);
    @Query(nativeQuery = true,
            value = "select * from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.nums_sold asc",
            countQuery = "select count(*) from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by  a.nums_sold desc ")
    Page<ProductInfo> getProductFilterAboutCategoryHaveSoldOutHigh(Integer categoryId, Long price, PageRequest of);

    @Query(nativeQuery = true,
            value = "select * from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by a.create_at desc",
            countQuery = "select count(*) from product a where (:categoryId is null or a.category_id = :categoryId)" +
                    "and (:price is null or a.price > :price)" +
                    "order by  a.create_at desc")
    Page<ProductInfo> getProductFilterAboutCategoryHaveCreateAt(Integer categoryId, Long price, PageRequest of);

    Page<Product> findBySalesGreaterThan(Double sales, Pageable pageable);





    @Query(nativeQuery = true,value = "SELECT COUNT(a.brand_id) FROM product a WHERE a.category_id = ?1 AND a.brand_id = ?2")
    Integer findBrandByCategory(Integer categoryId , Integer brandId);

    Page<ProductInfo> findByContentLikeIgnoreCaseOrNameContainsIgnoreCaseOrBrand_NameLikeIgnoreCaseOrCategory_NameContainsIgnoreCase(String content, String name, String name1, String name2,Pageable pageable);



}

