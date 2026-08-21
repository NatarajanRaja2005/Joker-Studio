package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.dto.UserDto;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.request.AddUserRequest;
import com.projoker.joker_studio.request.UpdateUserRequest;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.cart.ICartService;
import com.projoker.joker_studio.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {
    private final IUserService userService;
    private final ModelMapper modelMapper;
    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody AddUserRequest user){
        try {
            userService.createUser(user);
            return ResponseEntity.ok(new ApiResponse("Otp sent to User Successfully!",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User Creation Failed!",e.getMessage()));
        }
    }

    @PostMapping("/verify/email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String email,@RequestParam int otp){
        try {
            userService.verifyUserEmail(email,String.valueOf(otp));
            return ResponseEntity.ok(new ApiResponse("User Email was Verified Successfully",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Email verification Failed!",e.getMessage()));
        }
    }

    @PostMapping("/verify/phone")
    public ResponseEntity<ApiResponse> verifyPhone(@RequestParam Long phone,@RequestParam int otp){
        try {
            User newUser=userService.verifyUserPhone(phone,String.valueOf(otp));
            cartService.createCart(newUser.getId());
            UserDto userDto=modelMapper.map(newUser, UserDto.class);
            return ResponseEntity.ok(new ApiResponse("User Phone was verified Successfully",userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Phone verification Failed!",e.getMessage()));
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId,@RequestBody UpdateUserRequest user){
        try {
            User updatedUser=userService.updateUserDetails(userId,user);
            UserDto userDto=modelMapper.map(updatedUser, UserDto.class);
            return ResponseEntity.ok(new ApiResponse("User Updated Successfully :)",userDto));
        }
        catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not Exist",e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User Updation Failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long userId){
        try {
            User user= userService.getUser(userId);
            UserDto userDto=modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(new ApiResponse("User Retrived Successfully!",userDto));
        }catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not Exist",e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User Retrival Failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/email")
    public ResponseEntity<ApiResponse> getUser(@RequestParam String email){
        try {
            User user= userService.getUserByEmail(email);
            UserDto userDto=modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(new ApiResponse("User Retrived by email Successfully!",userDto));
        }catch (ItemNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not Exist",e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User retrival by email was failed!",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Deleted Successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("User deletion Failed",e.getMessage()));
        }
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email){
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok(new ApiResponse("Otp sent to email Successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Otp verification Failed",e.getMessage()));
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity<ApiResponse> changingPassword(@RequestParam String email,@RequestParam String password){
        try {
            User user= userService.changingPassword(email, password);
            UserDto userDto=modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(new ApiResponse("Password changed successfully.",userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Changing password Failed",e.getMessage()));
        }
    }
}
