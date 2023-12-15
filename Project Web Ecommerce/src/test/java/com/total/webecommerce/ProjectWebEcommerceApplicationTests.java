package com.total.webecommerce;

import com.total.webecommerce.entity.User;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ProjectWebEcommerceApplicationTests {

    @Autowired
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private RoleRepository roleRepository;
    @Test
    void contextLoads() {
    }

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


    @Test
void chang_infoUser(){
    User user = userRepository.findById(8).get();
    user.setPassword(encoder.encode("666666"));
    userRepository.save(user);
}


}
