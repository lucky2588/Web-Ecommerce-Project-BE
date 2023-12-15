package com.total.webecommerce;

import com.github.javafaker.Faker;
import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.dto.BestBuyer;
import com.total.webecommerce.entity.support.PaymentStatus;
import com.total.webecommerce.respository.OfAdmin.BrandRepository;
import com.total.webecommerce.respository.OfAdmin.DiscountRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfProduct.CommentProductRepository;
import com.total.webecommerce.respository.OfProduct.ProductRepository;
import com.total.webecommerce.respository.OfUser.FavoritesRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.respository.OrBlog.BlogRepository;
import com.total.webecommerce.respository.OrBlog.CommentOfBlogRepository;
import com.total.webecommerce.respository.OrOrder.OrderBillRepository;
import com.total.webecommerce.respository.OrOrder.OrderItemRepository;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SaveData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CommentOfBlogRepository comment;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private CommentProductRepository commentProductRepository;
    @Autowired
    private OrderBillRepository orders;
    @Autowired
    private OrderItemRepository orderItem;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DiscountRepository discountRepository;


//    @Test
//    void saveBrand(){
//        Brand brandLogitech = new Brand(1,"Logitech","https://www.nicepng.com/png/detail/177-1777709_logitech-gaming-logo-png-logitech-harmony-665-advanced.png");
//        Brand brandRazer = new Brand(2,"Razer","https://techalpha.vn/wp-content/uploads/2020/03/90758454_10159707225992576_1772984004690051072_n.png");
//        Brand brandHyperX = new Brand(3,"HyperX","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6LUfg4jbgExnMZLpQCkIOd9ZMU3ZllgJgFg&usqp=CAU");
//        Brand brandCorsair = new Brand(4,"Corsair","https://w7.pngwing.com/pngs/465/326/png-transparent-corsair-hd-logo.png");
//        Brand SteelSeries = new Brand(5,"SteelSeries","https://media.steelseriescdn.com/filer_public/c2/ec/c2ec1dd9-fb85-41f0-8517-7dd79aca81a3/ss_logo_icon_001.png");
//        brandRepository.save(brandLogitech);
//        brandRepository.save(brandRazer);
//        brandRepository.save(brandHyperX);
//        brandRepository.save(brandCorsair);
//        brandRepository.save(SteelSeries);
//    }

    @Test
    void saveBlog() {
        Faker fakeData = new Faker();
        List<User> users = userRepository.findByRoles_Name("AUTHOR");
        List<Brand> brands = brandRepository.findAll();
        Random rd = new Random();
        for (int i = 0; i < 15; i++) {
            Blog blog = Blog.builder()
                    .content("Content 1")
                    .title("Title 1")
                    .brand(brands.get(rd.nextInt(brands.size())))
                    .user(users.get(rd.nextInt(users.size())))
                    .thumbail(null)
                    .description("Mô Tả 1")
                    .build();
            blogRepository.save(blog);
        }
    }


//    @Test
//    void saveComment(){
//          List<User> users = userRepository.findByRoles_Name("USER");
//          List<Blog>  blogs = blogRepository.findAll();
//          Random rd = new Random();
//        for (int i = 0; i <blogs.size(); i++){
//            for (int j = 0; j <2 ; j++) {
//                CommentOfBlog comment = CommentOfBlog.builder()
//                        .user(users.get(rd.nextInt(users.size())))
//                        .content("Bình luận cho Blog "+i)
//                        .blog(blogs.get(i))
//                        .build();
//          commentOfBlogRepository.save(comment);
//            }
//        }
//    }

//    @Test
//    void save_product(){
//        Random rd = new Random();
//        List<String> namesProduct = List.of("Razer Product","Logitech G Pro" , "HyperX Product" , "Corsair Product" ,"Coirsair Product" );
//        List<Category> categories = categoryRepository.findAll();
//        List<Brand> brands = brandRepository.findAll();
//        for (int i = 0; i <45 ; i++) {
//            Product product = Product.builder()
//                    .brand(brands.get(rd.nextInt(brands.size())))
//                    .category(categories.get(rd.nextInt(categories.size())))
//                    .content("Content Product "+(i+1+45))
//                    .description(" description Product "+(i+1+45))
//                    .detail("Detail for Product "+(i+1+45))
//                    .nums(rd.nextInt(100))
//                    .price( (long) rd.nextInt(1000))
//                    .thumbail("https://hanoicomputercdn.com/media/product/47527_mouse_logitech_g_pro_wireless_gaming_2.jpg")
//                    .name(namesProduct.get(rd.nextInt(namesProduct.size())))
//                    .build();
//            productRepository.save(product);
//        }
//    }

    // Save Data Cmt for Product
    @Test
    void saveCommentOfProduct() {
        List<User> users = userRepository.findByRoles_Name("USER");
        List<Product> products = productRepository.findAll();
        Random rd = new Random();
        for (int i = 0; i < products.size(); i++) {
            for (int j = 0; j < 2; j++) {
                CommentProduct comment = CommentProduct.builder()
                        .user(users.get(rd.nextInt(users.size())))
                        .content("Bình luận cho Product  " + (i + 1))
                        .product(products.get(rd.nextInt(products.size())))
                        .build();
                commentProductRepository.save(comment);
            }
        }
    }
    @Test
    void save_img_forProduct(){
        List<String> ListIMG= new ArrayList<>(List.of("https://phudongphat.vn/wp-content/uploads/2023/04/toto-ms625dt2-800x800-1.jpg","https://hungtin.vn/wp-content/uploads/2021/08/voi-sen-toto-TBG02302V-TBW01008A-1000x1000-1.jpg","https://vn.toto.com/media/catalog/product/cache/1/small_image/300x/9df78eab33525d08d6e5fb8d27136e95/c/w/xcw166rb_-_tcw09s.jpg.pagespeed.ic.SsnJvm2jkP.jpg","https://www.tdm.vn/image/cache/catalog/product-4138/bon-tam-toto-PPY1806PWNE-740x740.jpg","https://vn.toto.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/u/s/uswn900as_1.png"));
        List<Product> productList1 = productRepository.findByCategory_Id(1);
        for (int i = 0; i <productList1.size() ; i++) {
            productList1.get(i).setThumbail("https://totovietnam.vn/wp-content/uploads/chau-rua-mat-toto-lht239c.jpg");
        }
        productRepository.saveAll(productList1);
        List<Product> productList2 = productRepository.findByCategory_Id(2);
        for (int i = 0; i <productList2.size() ; i++) {
            productList2.get(i).setThumbail("https://cdn.hita.com.vn/storage/products/toto/chau-rua-mat-lavabo-treo-tuong/lht328c-avt.jpg");
        }
        productRepository.saveAll(productList2);
        List<Product> productList3 = productRepository.findByCategory_Id(3);
        for (int i = 0; i <productList3.size() ; i++) {
            productList3.get(i).setThumbail("https://hangnhat360.com/wp-content/uploads/2020/08/sen-tam-toto-tbv03402j.jpg");
        }
        productRepository.saveAll(productList3);
        List<Product> productList4 = productRepository.findByCategory_Id(4);
        for (int i = 0; i <productList4.size() ; i++) {
            productList4.get(i).setThumbail("https://vlxdthanhtao.com.vn/public/images/product/details/bon-cau-toto-2-khoi-cs945dnt2-90075.jpg");
        }
        productRepository.saveAll(productList4);
        List<Product> productList5 = productRepository.findByCategory_Id(5);
        for (int i = 0; i <productList5.size() ; i++) {
            productList5.get(i).setThumbail("https://totovietnam.vn/wp-content/uploads/bo-phu-kien-toto-ys408n5v.jpg");
        }
        productRepository.saveAll(productList5);
        List<Product> productList6 = productRepository.findByCategory_Id(6);
        for (int i = 0; i <productList6.size() ; i++) {
            productList6.get(i).setThumbail("https://vn.toto.com/media/catalog/category/xtieunam.jpg.pagespeed.ic.spxotTZ9kX.jpg");
        }
        productRepository.saveAll(productList6);
        List<Product> productList100 = productRepository.findByCategory_Id(100);
        for (int i = 0; i <productList100.size() ; i++) {
            productList100.get(i).setThumbail("https://flux.vn/wp-content/uploads/2021/08/tbw02002b1_tbg02302v_tbw02017a-400x400.jpg");
        }
        productRepository.saveAll(productList100);
    }

    @Test
    void save_name_forProduct(){
        List<String> ListIMG= new ArrayList<>(List.of("https://phudongphat.vn/wp-content/uploads/2023/04/toto-ms625dt2-800x800-1.jpg","https://hungtin.vn/wp-content/uploads/2021/08/voi-sen-toto-TBG02302V-TBW01008A-1000x1000-1.jpg","https://vn.toto.com/media/catalog/product/cache/1/small_image/300x/9df78eab33525d08d6e5fb8d27136e95/c/w/xcw166rb_-_tcw09s.jpg.pagespeed.ic.SsnJvm2jkP.jpg","https://www.tdm.vn/image/cache/catalog/product-4138/bon-tam-toto-PPY1806PWNE-740x740.jpg","https://vn.toto.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/u/s/uswn900as_1.png"));
        List<Product> productList1 = productRepository.findByCategory_Id(1);
        for (int i = 0; i <productList1.size() ; i++) {
            productList1.get(i).setName("Sen Vòi TOTO");
        }
        productRepository.saveAll(productList1);
        List<Product> productList2 = productRepository.findByCategory_Id(2);
        for (int i = 0; i <productList2.size() ; i++) {
            productList2.get(i).setName("Chậu Rửa TOTO");
        }
        productRepository.saveAll(productList2);
        List<Product> productList3 = productRepository.findByCategory_Id(3);
        for (int i = 0; i <productList3.size() ; i++) {
           productList3.get(i).setName("Bồn Tắm");
        }
        productRepository.saveAll(productList3);
        List<Product> productList4 = productRepository.findByCategory_Id(4);
        for (int i = 0; i <productList4.size() ; i++) {
            productList4.get(i).setName("Bồn Cầu TOTO");
        }
        productRepository.saveAll(productList4);
        List<Product> productList5 = productRepository.findByCategory_Id(5);
        for (int i = 0; i <productList5.size() ; i++) {
            productList5.get(i).setName("Phụ Kiện");
        }
        productRepository.saveAll(productList5);
        List<Product> productList6 = productRepository.findByCategory_Id(6);
        for (int i = 0; i <productList6.size() ; i++) {
            productList6.get(i).setName("Khu Công Cộng");
        }
        productRepository.saveAll(productList6);
        List<Product> productList100 = productRepository.findByCategory_Id(100);
        for (int i = 0; i <productList100.size() ; i++) {
            productList100.get(i).setName("Phụ Kiện Khác");
        }
        productRepository.saveAll(productList100);
    }

    @Test
    void save_Favorites() {
        Favorites favorites = new Favorites();
        User user = userRepository.findById(17).get();
        favorites.setUser(user);
        List<Product> products = productRepository.findAllById(List.of(65, 48));
        Set<Product> products1 = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            products1.add(products.get(i));
        }
        favorites.setProducts(products1);
        favoritesRepository.save(favorites);
    }


    @Test
    void create_price_save() {
        Random rd = new Random();
        List<Discount> discounts = discountRepository.findAll();
        for (int i = 0; i < 30; i++) {
            Product item = productRepository.findById(rd.nextInt(productRepository.findAll().size())).get();
            Discount acc = discounts.get(rd.nextInt(discounts.size()));
            item.setDiscount(acc);
            productRepository.save(item);
        }
    }


    @Test
    void save_price_for_Ghe() {
        Random rd = new Random();
        double minPrice = 1000000.0;
        double maxPrice = 5000000.0;
        List<Product> products = productRepository.findByCategory_Id(6);
        for (int i = 0; i < products.size(); i++) {
            double randomPrice = rd.nextDouble(minPrice, maxPrice);
            double roundedNumber = Math.round(randomPrice * 100.0) / 100.0;
            products.get(i).setPrice(roundedNumber);
            if (products.get(i).getDiscount() == null) {
                products.get(i).setSales(0.0);
            }
            productRepository.save(products.get(i));
        }
    }


    @Test
    void save_product() {
        List<Integer> numsOut = List.of(0, 1, 2, 3, 8);
        Random rd = new Random();
        List<Product> products = productRepository.findAll();
        for (int i = 0; i < products.size(); i++) {
            products.get(i).setNumsSold(rd.nextInt(numsOut.size()));
            productRepository.save(products.get(i));
        }
    }



    @Test
    @Rollback
    void getPorductBestSeller() {
        List<BestBuyer> lists = paymentRepository.getBuyer(PaymentStatus.SUCCESS);
        lists.forEach(System.out::println);
    }

    @Test
    void save_blog_public(){
        List<Blog> blogs = blogRepository.findAll();
        for (int i = 0; i < blogs.size() ; i++) {
            blogs.get(i).setStatusBlog(1);
            blogRepository.save(blogs.get(i));
        }
    }


}











