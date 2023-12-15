package com.total.webecommerce.service;

import com.total.webecommerce.entity.*;
import com.total.webecommerce.entity.support.NotificationStatus;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.exception.ConfirmTokenException;
import com.total.webecommerce.exception.NotFoundException;
import com.total.webecommerce.mapper.UserMapper;
import com.total.webecommerce.response.AuthResponse;
import com.total.webecommerce.respository.OfAdmin.NotificationRepository;
import com.total.webecommerce.respository.OfAdmin.EmailRepository;
import com.total.webecommerce.respository.OfAdmin.RoleRepository;
import com.total.webecommerce.respository.OfAdmin.TokenRepository;
import com.total.webecommerce.respository.OfUser.UserRepository;
import com.total.webecommerce.resquest.OfAuth.LoginResquest;
import com.total.webecommerce.resquest.OfAuth.RegisterResquest;
import com.total.webecommerce.security.CustomUserDetailService;
import com.total.webecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailService emailService;
    public AuthResponse login(LoginResquest resquestLogin) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(resquestLogin.getEmail(), resquestLogin.getPassword());
        // tien hanh xac thuc
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            //  Luu vao database Context Horder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Lấy ra thông tin UserDetails()
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());
            String jwtToken = jwtUtils.generateToken(userDetails);
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);

            return
                    new AuthResponse(
                            UserMapper.toUserDto(user),
                            jwtToken, true
                    );
        } catch (Exception ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    // Đăng kí tài khoản => todo : check tài khoản với email , tạo user và gửi về mã token để xác nhận
    public ResponseEntity<?> registerAccount(RegisterResquest resquest) {
        Optional<User> users = userRepository.findByEmail(resquest.getEmail());
        if (users.isPresent()) { // nếu đã có tài khoản
            if (users.get().isEnabled()) {
                throw new BadResquestException("Email này đã được sử dụng");
            }
            if (!users.get().isEnabled()) {
                String tokenNew = UUID.randomUUID().toString();
                Token token = Token.builder()
                        .tokenUser(tokenNew)
                        .user(users.get())
                        .build();
                tokenRepository.save(token);

                emailService.sendEmail(resquest.getEmail(), "Xác mình tài khoản", "http://localhost:8888/confimToken/" + token.getTokenUser());
                return ResponseEntity.ok("Email này đã được đăng kí nhưng chưa kích hoạt , chúng tôi đã gửi mã kích hoạt của bạn ở phần Email");
            }
        }
        Role role = roleRepository.findByName("USER").orElse(null);
        User user = User.builder()
                .email(resquest.getEmail())
                .phone(resquest.getPhoneNumber())
                .name(resquest.getName())
                .address(resquest.getAddress())
                .password(encoder.encode(resquest.getPassword()))
                .roles(List.of(role))
                .build();
        userRepository.save(user);
        String newUUID = UUID.randomUUID().toString();
        Token token = Token.builder()
                .user(user)
                .tokenUser(newUUID)
                .build();
        tokenRepository.save(token);
        Notification notification = Notification.builder()
                .username(user.getName())
                .title("Register Account")
                .content(user.getName()+" Create Account New with Email " +user.getEmail())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        emailService.sendEmail(resquest.getEmail(), "Xác mình tài khoản", "Mã Token của bạn : " + token.getTokenUser());
        return ResponseEntity.ok("Khởi tạo mật khẩu thành công, hãy đăng nhập địa chỉ Email của bạn đã xác minh tài khoản ");
    }

    public ResponseEntity<?> confirmUser(String tokenUser) {
        Optional<Token> token = tokenRepository.findByTokenUser(tokenUser);
        if (token.isEmpty()) {
            throw new BadResquestException("Không tìm thấy Token này !!! ");
        }
        if (LocalDateTime.now().isAfter(token.get().getExpiresAt())) {
            String newUUID = UUID.randomUUID().toString();
            Token tokenNew = Token.builder()
                    .user(token.get().getUser())
                    .tokenUser(newUUID)
                    .build();
            tokenRepository.save(tokenNew);
            emailService.sendEmail(token.get().getUser().getEmail(), "xác minh lại tài khoản của bạn", "Mã Token của bạn : " + tokenNew.getTokenUser());
            throw new ConfirmTokenException("Token này đã hết hạn , chúng tôi đã gửi 1 mã Token mới cho bạn tại Email , hãy điền Token mới vào đây !! ");
        }
        User user = token.get().getUser();
        user.setIsEnable(true);
        userRepository.save(user);
        return ResponseEntity.ok("Xác Thực thành công ");
    }

    public ResponseEntity<?> confirmEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BadResquestException("Không tìm thấy Email của bạn hãy kiểm tra lại email đã nhập ");
        }
        String tokenConfirm = UUID.randomUUID().toString();
        Token token = Token.builder()
                .tokenUser(tokenConfirm)
                .user(userOptional.get())
                .build();
        tokenRepository.save(token);
       Notification notification = Notification.builder()
               .username(userOptional.get().getName())
               .title("Forget Password")
               .avatar(userOptional.get().getAvatar())
               .content(userOptional.get().getName()+" Forget Password , send token to Email "+userOptional.get().getEmail())
               .typeOf(0)
               .notificationStatus(NotificationStatus.ACCOUNT)
               .build();
       notificationRepository.save(notification);
       emailService.sendEmail(userOptional.get().getEmail(), "Xác minh tài khoản ", "Mã Code để xác minh tài khoản của bạn : " + token.getTokenUser());
        return ResponseEntity.ok("Chúng tôi đã gửi mã code qua Email của bạn !!");
    }

    public ResponseEntity<?> confirmTokenAndEmail(String email, String token) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Token> tokenOptional = tokenRepository.findByTokenUser(token);
        if(tokenOptional.isEmpty()){
            throw new NotFoundException("Mã Token không đúng , hoặc không hợp lệ , vui lòng kiểm tra lại");
        }
        if(user.get().getId() != tokenOptional.get().getUser().getId()){
            throw new ConfirmTokenException("Token này không hợp lệ");
        }
       if(tokenOptional.get().getExpiresAt().isAfter(LocalDateTime.now().plusMinutes(5))){
           throw new BadResquestException("Mã Token này đã hết hạn");
       }
       String passNew = UUID.randomUUID().toString();
       user.get().setPassword(encoder.encode(passNew));
       userRepository.save(user.get());
        Notification notification = Notification.builder()
                .username(user.get().getUsername())
                .title("Confirm Email")
                .avatar(user.get().getAvatar())
                .content(user.get().getName() +" Comfirm Password  with Email " +user.get().getEmail())
                .notificationStatus(NotificationStatus.ACCOUNT)
                .typeOf(0)
                .build();
        notificationRepository.save(notification);
        Email email1 = Email.builder()
                .email(email)
                .title("Mật Khẩu Mới Của Bạn")
                .content("Mật Khẩu : "+passNew)
                .build();
        emailRepository.save(email1);
       emailService.sendEmail(email,"Mật Khẩu Mới Của Bạn","Mật Khẩu : "+passNew);
       return ResponseEntity.ok("Xác thực Token thành công , chúng tôi đã gửi cho bạn mật khẩu mới ở Email của bạn !! hãy dùng đăng nhập và vào đổi mật khẩu .. ");
    }








}
