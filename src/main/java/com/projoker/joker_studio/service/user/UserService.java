package com.projoker.joker_studio.service.user;

import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.exception.VerificationFailedException;
import com.projoker.joker_studio.model.*;
import com.projoker.joker_studio.repository.EmailVerificationRepository;
import com.projoker.joker_studio.repository.SmsVerificationRepository;
import com.projoker.joker_studio.repository.UserRepository;
import com.projoker.joker_studio.request.AddUserRequest;
import com.projoker.joker_studio.request.UpdateUserRequest;
import com.projoker.joker_studio.service.cart.ICartService;
import com.projoker.joker_studio.service.notification.INotificationService;
import com.projoker.joker_studio.service.notification.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final INotificationService notificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final SmsVerificationRepository smsVerificationRepository;
    private final SmsService smsService;
    private final ICartService cartService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(AddUserRequest user) {
        User newUser = userRepository.findByEmail(user.getEmail());
        if (newUser != null) {
            throw new AlreadyExistException("User Already Exists!");
        }
        newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhoneVerification(true);
        newUser.setAddress(user.getAddress());

        //System.out.println(user.getAddress().getCity());


        SecureRandom random = new SecureRandom();
        final int password = (100000 + random.nextInt(900000));

        EmailVerification emailVerification=new EmailVerification();
        emailVerification.setEmail(user.getEmail());
        emailVerification.setOtp(passwordEncoder.encode(String.valueOf(password)));
        //here is setting expiry time
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        emailVerification.setVerified(false);

        notificationService.optVerification(user.getEmail(), String.valueOf(password));
        emailVerification.setVerified(false);
        emailVerificationRepository.save(emailVerification);


        final int phonePassword=(100000 + random.nextInt(900000));

        SmsVerification smsVerification=new SmsVerification();
        smsVerification.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        smsVerification.setOtp(String.valueOf(phonePassword));
        smsVerification.setPhone(user.getPhone());

        //smsService.otpVerification(user.getPhone(),String.valueOf(phonePassword));
        //smsVerification.setVerified(false);
        //smsVerificationRepository.save(smsVerification);

        userRepository.save(newUser);
        cartService.createCart(newUser.getId());
    }

    @Override
    public User verifyUserEmail(String email, String password){
        EmailVerification verify=emailVerificationRepository.findTopByEmailOrderByExpiresAtDesc(email);
        if(password.isEmpty()){
            throw new VerificationFailedException("Kindly Ensure 6 digit OTP");
        }
        if(!passwordEncoder.matches(password,verify.getOtp())){
            throw new VerificationFailedException("Invalid OTP!");
        }
        if(!verify.getExpiresAt().isAfter(LocalDateTime.now())){
            throw new VerificationFailedException("Otp expired.");
        }
        if(verify.isVerified()){
            throw new RuntimeException("User is already verified");
        }
        verify.setVerified(true);
        emailVerificationRepository.save(verify);
        User user=userRepository.findByEmail(email);
        user.setEmailVerification(true);
        return userRepository.save(user);
    }

    @Override
    public User verifyUserPhone(Long phone, String password) {
        SmsVerification smsVerification=smsVerificationRepository.findByPhone(phone);
        User user=userRepository.findByPhone(phone);
        if(user==null || smsVerification==null){
            throw new VerificationFailedException("Unknown phone number");
        }
        if(user.isPhoneVerification()){
            throw new AlreadyExistException("Invalid phone number. Account already exists.");
        }
        if(password.isEmpty()){
            throw new VerificationFailedException("Kindly Ensure 6 digit OTP");
        }
        if(!smsVerification.getOtp().equals(password)){
            throw new VerificationFailedException("Invalid OTP!");
        }
        if(!smsVerification.getExpiryTime().isAfter(LocalDateTime.now())){
            throw new VerificationFailedException("OTP expired.");
        }
        if(user.isPhoneVerification()){
            throw new AlreadyExistException("User is already Verified by phone");
        }
        smsVerification.setVerified(true);
        user.setPhoneVerification(true);
        smsVerificationRepository.save(smsVerification);

        return userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) {
        User user=userRepository.findByEmail(email);
        EmailVerification emailVerification=new EmailVerification();
        emailVerification.setVerified(false);
        emailVerification.setEmail(email);
        SecureRandom random=new SecureRandom();
        final int pass=(100000+random.nextInt(900000));
        emailVerification.setOtp(passwordEncoder.encode(String.valueOf(pass)));
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        user.setEmailVerification(false);
        userRepository.save(user);
        emailVerificationRepository.save(emailVerification);
        notificationService.optVerification(email,"Joker Studio:For Resetting Password OTP is: "+pass);
    }

    @Override
    public User changingPassword(String email, String password){
        User user=userRepository.findByEmail(email);
        if(!user.isEmailVerification()){
            throw new VerificationFailedException("Not an verified user!");
        }
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public User updateUserDetails(Long userId, UpdateUserRequest user) {
        User existUser=userRepository.findByEmail(user.getEmail());
        if(existUser==null){
            throw new ItemNotExistException("User Not Exists!");
        }
        if(!existUser.isEmailVerification() || !existUser.isPhoneVerification()){
            throw new VerificationFailedException("User is not verified");
        }
        existUser.setFirstName(user.getFirstName());
        existUser.setLastName(user.getLastName());
        existUser.setEmail(user.getEmail());
        existUser.setPhone(user.getPhone());
        existUser.setAddress(user.getAddress());
        userRepository.save(existUser);
        return existUser;
    }

    @Override
    public User getUser(Long userId) {
        //System.out.println("Get called with: "+userId);
        Optional<User> existUser=userRepository.findById(userId);
        if(existUser.isEmpty()){
            throw new ItemNotExistException("User Not Exists: "+userId);
        }

        if(!existUser.get().isEmailVerification() || !existUser.get().isPhoneVerification()){
            throw new VerificationFailedException("User is not verified");
        }

        return existUser.get();
    }

    //Can't be accessed by user, only acts as an helper methods
    @Override
    public void deleteUser(Long userId) {
        Optional<User> existUser=userRepository.findById(userId);
        if(existUser.isEmpty()){
            throw new ItemNotExistException("User Not Exists!");
        }
        User user=existUser.get();
        user.setDeactivate(true);
        user.setEmail(user.getEmail()+"DEACTIVATED");
        userRepository.save(user);
    }

    //It is also acts as a Helper method. So don't provide exception here. Done it on controller
    @Override
    public User getUserByEmail(String email) {
        User user=userRepository.findByEmail(email);
        if(!user.isEmailVerification()){
            throw new VerificationFailedException("User Email is not verified");
        }
        return user;
    }

    @Override
    public User getUserByPhone(Long phone){
        User user=userRepository.findByPhone(phone);
        if(!user.isEmailVerification() || !user.isPhoneVerification()){
            throw new VerificationFailedException("User Phone is not Verified!");
        }
        return user;
    }

}
