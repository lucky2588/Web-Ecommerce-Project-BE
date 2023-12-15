package com.total.webecommerce;

import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.CategoryRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CloneData {
    @Autowired
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;



//    @Test
//    void save_users() {
//        Role roleUser = roleRepository.findByName("USER").orElse(null);
//        Role roleAdmin = roleRepository.findByName("ADMIN").orElse(null);
//        Role roleAuthor = roleRepository.findByName("AUTHOR").orElse(null);
//        User user1 = new User(1, "Đúc Thắng", "thang@123", passwordEncoder.encode("111"), null, List.of(roleAdmin));
//        User user2 = new User(2, "Huyền Trang", "trang@123", passwordEncoder.encode("111"), null, List.of(roleUser));
//        User user3 = new User(3, "Cao Trang", "trang@456", passwordEncoder.encode("111"), null, List.of(roleAuthor));
//        User user4 = new User(4, "Mai Linh", "linh@123", passwordEncoder.encode("111"), null, List.of(roleAuthor));
//        User user5 = new User(5, "Han So Hee", "kr@123", passwordEncoder.encode("111"), null, List.of(roleAuthor));
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//        userRepository.save(user5);
//    }


//    @Test
//    void save_product(){
//        Random rd = new Random();
//        List<String> namesProduct = List.of("Razer Product","Logitech G Pro" , "HyperX Product" , "Corsair Product" ,"Coirsair Product" );
//         List<Category> categories = categoryRepository.findAll();
//         List<Brand> brands = brandRepository.findAll();
//        for (int i = 0; i <45 ; i++) {
//            Product product = Product.builder()
//                    .brand(brands.get(rd.nextInt(brands.size())))
//                    .category(categories.get(rd.nextInt(categories.size())))
//                    .content("Content Product "+(i+1))
//                    .description(" description Product "+(i+1))
//                    .detail("Detail for Product "+(i+1))
//                    .nums(rd.nextInt(100))
//                    .price((long )rd.nextInt(1000))
//                    .thumbail("https://hanoicomputercdn.com/media/product/47527_mouse_logitech_g_pro_wireless_gaming_2.jpg")
//                    .name(namesProduct.get(rd.nextInt(namesProduct.size())))
//                    .build();
//            productRepository.save(product);
//        }
//    }


}
