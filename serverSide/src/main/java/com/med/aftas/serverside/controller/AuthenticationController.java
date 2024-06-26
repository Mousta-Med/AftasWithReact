package com.med.aftas.serverside.controller;

import com.med.aftas.serverside.dto.UserDto;
import com.med.aftas.serverside.dto.AuthenticationRequest;
import com.med.aftas.serverside.dto.respDto.AuthenticationResponse;
import com.med.aftas.serverside.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid UserDto userDto){
        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(userService.login(authenticationRequest));
    }
}
