package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.request.LoginRequest;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.response.JwtResponse;
import com.projoker.joker_studio.security.jwt.JwtUtil;
import com.projoker.joker_studio.security.user_details.StudioUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request){
        try {
            Authentication authentication=
                    authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token=jwtUtil.generateToken(authentication);
            StudioUserDetails userDetails= (StudioUserDetails) authentication.getPrincipal();
            JwtResponse response=new JwtResponse(userDetails.getId(),token);
            return ResponseEntity.ok(new ApiResponse("Login Success",response));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
