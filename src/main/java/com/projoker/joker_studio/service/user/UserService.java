package com.projoker.joker_studio.service.user;

import com.projoker.joker_studio.dto.UserDto;
import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.OrderAddress;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.UserRepository;
import com.projoker.joker_studio.request.AddUserRequest;
import com.projoker.joker_studio.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    public final UserRepository userRepository;

    @Override
    public User createUser(AddUserRequest user) {
        User newUser=getUserByEmail(user.getEmail());
        if(newUser!=null){
            throw new AlreadyExistException("User Already Exists!");
        }
        newUser=new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setPassword(user.getPassword());

        newUser.setAddress(user.getAddress());
        //System.out.println(user.getAddress().getCity());
        return userRepository.save(newUser);
    }

    @Override
    public User updateUserDetails(Long userId, UpdateUserRequest user) {
        User existUser=getUser(userId);
        if(existUser==null){
            throw new ItemNotExistException("User Not Exists!");
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
        return existUser.get();
    }

    //Can't be accessed by user, only acts as an helper methods
    @Override
    public void deleteUser(Long userId) {
        User existUser=getUser(userId);
        if(existUser==null){
            throw new ItemNotExistException("User Not Exists!");
        }
        userRepository.delete(existUser);
    }

    //It is also acts as a Helper method. So don't provide exception here. Done it on controller
    @Override
    public User getUserByEmail(String email) {
        User user=userRepository.findByEmail(email);
        return user;
    }

}
