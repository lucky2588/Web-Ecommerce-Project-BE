package com.total.webecommerce.controller;

import com.total.webecommerce.response.AuthResponse;
import com.total.webecommerce.resquest.OfUser.ForgetPassword;
import com.total.webecommerce.resquest.OfAuth.LoginResquest;
import com.total.webecommerce.resquest.OfAuth.RegisterResquest;
import com.total.webecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("login")
    public AuthResponse login(@RequestBody LoginResquest loginResquest){
        AuthResponse authResponse = authService.login(loginResquest);
        return authResponse;
    };

    @PostMapping("register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody RegisterResquest resquest){
        authService.registerAccount(resquest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PostMapping("confirmToken/{token}")
    public ResponseEntity<?> confirmUser(@PathVariable String token){
        return authService.confirmUser(token);
    }

   @PostMapping("confirmEmail/{email}")
    public ResponseEntity<?> confirmEmail(@PathVariable String email){
        return authService.confirmEmail(email);
    }


   @PostMapping("confirmPassword")
    public ResponseEntity<?> confirmEmailAndToken(@Valid @RequestBody ForgetPassword resquest) {
       return authService.confirmTokenAndEmail(resquest.getEmail() , resquest.getToken());
   }



}
