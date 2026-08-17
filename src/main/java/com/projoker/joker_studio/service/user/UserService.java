package com.projoker.joker_studio.service.user;

import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.exception.VerificationFailedException;
import com.projoker.joker_studio.model.EmailVerification;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.EmailVerificationRepository;
import com.projoker.joker_studio.repository.UserRepository;
import com.projoker.joker_studio.request.AddUserRequest;
import com.projoker.joker_studio.request.UpdateUserRequest;
import com.projoker.joker_studio.service.notification.INotificationService;
import lombok.RequiredArgsConstructor;
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
        newUser.setPassword(user.getPassword());

        newUser.setAddress(user.getAddress());
        //System.out.println(user.getAddress().getCity());


        SecureRandom random = new SecureRandom();
        final int password = (100000 + random.nextInt(900000));

        EmailVerification emailVerification=new EmailVerification();
        emailVerification.setEmail(user.getEmail());
        emailVerification.setOtp(String.valueOf(password));
        //here is setting expiry time
        emailVerification.setExperiesAt(LocalDateTime.now().plusMinutes(10));
        emailVerification.setVerified(false);

        notificationService.optVerification(user.getEmail(), String.valueOf(password));
        emailVerification.setVerified(false);
        emailVerificationRepository.save(emailVerification);

        userRepository.save(newUser);
    }

    @Override
    public User verifyUser(String email, String password){
        EmailVerification verify=emailVerificationRepository.findByEmail(email);
        if(password.isEmpty()){
            throw new VerificationFailedException("Kindly Ensure 6 digit OTP");
        }
        if(!verify.getOtp().equals(password)){
            throw new VerificationFailedException("Invalid OTP!");
        }
        if(!verify.getExperiesAt().isAfter(LocalDateTime.now())){
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
    public User updateUserDetails(Long userId, UpdateUserRequest user) {
        User existUser=userRepository.findByEmail(user.getEmail());
        if(existUser==null){
            throw new ItemNotExistException("User Not Exists!");
        }
        if(!existUser.isEmailVerification()){
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

        if(!existUser.get().isEmailVerification()){
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
            throw new VerificationFailedException("User is not verified");
        }
        return user;
    }

}
