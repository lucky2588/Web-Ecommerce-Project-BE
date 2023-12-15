package com.total.webecommerce;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.projection.Public.CommentBlog;
import com.total.webecommerce.response.AnalystRes;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.CategoryRepository;
import com.total.webecommerce.respository.OfAdmin.DiscountRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import com.total.webecommerce.respository.OrBlog.CommentOfBlogRepository;
import com.total.webecommerce.respository.OrOrder.OrderBillRepository;
import com.total.webecommerce.respository.OrOrder.OrderItemRepository;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UnilsTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CommentOfBlogRepository comment;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private OrderBillRepository orders;
   @Autowired
   private OrderItemRepository orderItem;
   @Autowired
   private PaymentRepository paymentRepository;
    @Autowired
    private DiscountRepository discountRepository;

    @Test
    public void lookRole() {
        List<User> Users = userRepository.findByRoles_Name("AUTHOR");
        Users.forEach(System.out::println);
    }

    @Test
    @Rollback(value = false)
    public void getBlogHaveView() {
        List<Blog> blogs = blogRepository.getBlogsHaveViewTop();
        blogs.forEach(System.out::println);
    }



    @Test
    void deleteCmtBlog() {
        CommentOfBlog cmts = comment.findById(41).get();
        comment.delete(cmts);
    }

    @Test
    void deleteBlog() {
        Blog blog = blogRepository.findById(19).get();
        blogRepository.delete(blog);

    }

    @Test
    void deleteBrand() {
        Brand brand = brandRepository.findById(6).get();
        brandRepository.delete(brand);

    }

    @Test
    void deleteProduct() {
        Product product = productRepository.findById(21).get();
        productRepository.delete(product);
    }

    @Test
    void deleteFavotires() {
        Favorites favorites = favoritesRepository.findById(1).get();
        favoritesRepository.delete(favorites);
    }


    @Test
    void countBrand() {
        Integer size = productRepository.findBrandByCategory(1, 1);
        System.out.println(size);
    }

    @Test
    void RemoveUser() {
        Optional<User> user = userRepository.findById(20);
        userRepository.delete(user.get());
    }

    @Test
    void RemoveOrder() {
        List<Integer> ids = List.of(3, 4, 5, 6, 7, 8);
        orders.deleteAllById(ids);
    }

    @Test
    void ChangleProduct() {
        Product item = productRepository.findById(65).get();
        Discount biuldProduct = discountRepository.findById(2).get();
        item.setDiscount(biuldProduct);
        productRepository.save(item);
    }

    @Test
    void Remove_Favorites() {
        Favorites item = favoritesRepository.findById(7).get();
        favoritesRepository.delete(item);
    }

    @Test
    void deletePayment() {
        Payment payment = paymentRepository.findById(22).get();
        paymentRepository.delete(payment);
    }

    @Test
    void getSale(){
        LocalDate localDate = LocalDate.of(2023, 7, 25);
        List<Object> getRes = paymentRepository.getSaleTargetForDay(localDate);
        getRes.forEach(System.out::println);
    }
    @Test
    void deleteOrderItem() {
        OrderItem item = orderItem.findById(145).get();
        orderItem.delete(item);
    }
    @Test
    void deleteOrderBill() {
        OrderBill item = orders.findById(35).get();
        orders.delete(item);
    }
    @Test
    void findNull() {
        List<OrderItem> item = orderItem.findOrderItemIsNull();
        item.forEach(System.out::println);
    }

    @Test
    void find_idBlog(){
        Blog blog = blogRepository.findAll().get(blogRepository.findAll().size()-1);
        System.out.println(blog.getId());
    }


    @Test
    void deleteCategory(){
        Category item = categoryRepository.findById(77).orElseThrow(null);
        categoryRepository.delete(item);
    }


}
